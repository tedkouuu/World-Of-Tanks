package com.example.world_of_tanks.AWSS3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class S3Service {

    private static final int PRESIGN_MINUTES = 720;
    private static final Duration CACHE_TTL = Duration.ofMinutes(600);
    private static final Duration NEGATIVE_CACHE_TTL = Duration.ofMinutes(10);
    private static final long CIRCUIT_OPEN_MS = 30_000;

    private final String bucket;
    private final S3Client s3;
    private final S3Presigner presigner;
    private final boolean enabled;

    private final ConcurrentHashMap<String, CachedEntry> urlCache = new ConcurrentHashMap<>();
    private volatile long circuitOpenUntil = 0;

    private record CachedEntry(URL url, boolean found, Instant expiresAt) {
        boolean isValid() { return Instant.now().isBefore(expiresAt); }
    }

    public S3Service(
            @Value("${aws.s3.bucket:}") String bucket,
            @Value("${aws.region:}") String region
    ) {
        this.bucket = bucket == null ? "" : bucket.trim();
        var regValue = region == null ? "" : region.trim();

        if (this.bucket.isEmpty() || regValue.isEmpty()) {
            this.enabled = false;
            this.s3 = null;
            this.presigner = null;
            return;
        }

        this.enabled = true;
        var creds = DefaultCredentialsProvider.create();
        var reg = Region.of(regValue);

        this.s3 = S3Client.builder()
                .region(reg)
                .credentialsProvider(creds)
                .overrideConfiguration(ClientOverrideConfiguration.builder()
                        .apiCallTimeout(Duration.ofSeconds(3))
                        .apiCallAttemptTimeout(Duration.ofSeconds(2))
                        .build())
                .build();

        this.presigner = S3Presigner.builder()
                .region(reg)
                .credentialsProvider(creds)
                .build();
    }

    /**
     * Returns a presigned URL from cache or S3, or null if unavailable.
     * Uses circuit breaker to skip S3 when it's unresponsive.
     */
    public URL getPresignedUrl(String key) {
        if (!enabled || isCircuitOpen()) {
            return null;
        }

        var cached = urlCache.get(key);
        if (cached != null && cached.isValid()) {
            return cached.found() ? cached.url() : null;
        }

        try {
            if (!existsInternal(key)) {
                urlCache.put(key, new CachedEntry(null, false, Instant.now().plus(NEGATIVE_CACHE_TTL)));
                return null;
            }
            var url = presignGetInternal(key, PRESIGN_MINUTES);
            urlCache.put(key, new CachedEntry(url, true, Instant.now().plus(CACHE_TTL)));
            resetCircuit();
            return url;
        } catch (Exception e) {
            openCircuit();
            return null;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public URL presignGet(String key, int minutes) {
        if (!enabled) {
            throw new IllegalStateException("S3 is disabled");
        }
        return presignGetInternal(key, minutes);
    }

    public boolean exists(String key) {
        if (!enabled) {
            return false;
        }
        return existsInternal(key);
    }

    private boolean isCircuitOpen() {
        return System.currentTimeMillis() < circuitOpenUntil;
    }

    private void openCircuit() {
        circuitOpenUntil = System.currentTimeMillis() + CIRCUIT_OPEN_MS;
    }

    private void resetCircuit() {
        circuitOpenUntil = 0;
    }

    private URL presignGetInternal(String key, int minutes) {
        var get = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        var req = GetObjectPresignRequest.builder()
                .getObjectRequest(get)
                .signatureDuration(Duration.ofMinutes(minutes))
                .build();

        return presigner.presignGetObject(req).url();
    }

    private boolean existsInternal(String key) {
        try {
            var head = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3.headObject(head);
            return true;
        } catch (NoSuchKeyException ex) {
            return false;
        } catch (SdkClientException | S3Exception ex) {
            return false;
        }
    }
}
