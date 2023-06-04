package com.example.parkar;

import com.google.gson.annotations.SerializedName;

public class FileUploadRequest {
    @SerializedName("fileUrl")
    private String fileUrl;

    public FileUploadRequest(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    // Getters and setters (if needed)
}
