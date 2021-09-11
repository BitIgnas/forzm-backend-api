package org.forzm.demo.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.forzm.demo.service.FileService;
import org.forzm.demo.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final AmazonS3Client amazonS3Client;

    @Override
    public String uploadFile(MultipartFile file, String bucket) {
        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileKey = UUID.randomUUID().toString().concat("." + filenameExtension);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);
        } catch (IOException ioException) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        amazonS3Client.setObjectAcl(bucket, fileKey, CannedAccessControlList.PublicRead);
        return amazonS3Client.getResourceUrl(bucket, fileKey);
    }

    @Override
    public void deleteFile(String url, String bucketName) {
        String key = url.substring(url.lastIndexOf('/')+1);

        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
