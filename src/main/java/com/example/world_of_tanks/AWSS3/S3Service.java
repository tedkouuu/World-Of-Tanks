package com.example.world_of_tanks.AWSS3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
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

@Service
public class S3Service {

    private final String bucket;
    private final S3Client s3;
    private final S3Presigner presigner;
    private final boolean enabled;

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
        this.s3 = S3Client.builder().region(reg).credentialsProvider(creds).build();
        this.presigner = S3Presigner.builder().region(reg).credentialsProvider(creds).build();
    }

    public URL presignGet(String key, int minutes) {
        if (!enabled) {
            throw new IllegalStateException("S3 is disabled");
        }
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

    public boolean exists(String key) {
        if (!enabled) {
            return false;
        }
        try {
            var head = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3.headObject(head);
            return true;
        } catch (NoSuchKeyException ex) {
            return false;
        } catch (SdkClientException ex) {
            return false;
        } catch (S3Exception ex) {
            return false;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}
