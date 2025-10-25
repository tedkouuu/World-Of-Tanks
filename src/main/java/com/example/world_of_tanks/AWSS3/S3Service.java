package com.example.world_of_tanks.AWSS3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class S3Service {
    private final String bucket;
    private final S3Client s3;
    private final S3Presigner presigner;

    public S3Service(@Value("${aws.s3.bucket}") String bucket,
                     @Value("${aws.region}") String region) {
        this.bucket = bucket;
        var creds = DefaultCredentialsProvider.create();
        var r = Region.of(region);
        this.s3 = S3Client.builder().region(r).credentialsProvider(creds).build();
        this.presigner = S3Presigner.builder().region(r).credentialsProvider(creds).build();
    }

    public URL presign(String key, int minutes) {
        var get = GetObjectRequest.builder().bucket(bucket).key(key).build();
        var req = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(minutes))
                .getObjectRequest(get).build();
        return presigner.presignGetObject(req).url();
    }
}