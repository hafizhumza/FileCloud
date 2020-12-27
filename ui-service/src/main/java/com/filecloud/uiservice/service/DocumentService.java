package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.DocumentServiceClient;
import com.filecloud.uiservice.client.response.DocumentResponse;
import com.filecloud.uiservice.client.response.SingleFieldResponse;
import com.filecloud.uiservice.client.response.SpaceInfoResponse;
import com.filecloud.uiservice.dto.mvcmodel.DocumentModel;
import com.filecloud.uiservice.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DocumentService extends BaseService {

    private final DocumentServiceClient documentServiceClient;

    @Autowired
    public DocumentService(DocumentServiceClient documentServiceClient) {
        this.documentServiceClient = documentServiceClient;
    }

    public String count(String bearerToken) {
        Result<SingleFieldResponse> result = documentServiceClient.count(bearerToken);
        throwIfInvalidAccess(result);
        return result.getData().getResponse().toString();
    }

    public SpaceInfoResponse spaceInfo(String bearerToken) {
        Result<SpaceInfoResponse> result = documentServiceClient.spaceInfo(bearerToken);
        throwIfInvalidAccess(result);
        return result.getData();
    }

    public List<DocumentModel> listDocuments(String bearerToken) {
        Result<List<DocumentResponse>> result = documentServiceClient.listDocuments(bearerToken);
        throwIfInvalidAccess(result);
        return result.getData().stream().map(DocumentModel::new).collect(Collectors.toList());
    }

    public Result<DocumentResponse> getDocument(String bearerToken, long id) {
        Result<DocumentResponse> result = documentServiceClient.getDocument(bearerToken, id);
        throwIfInvalidAccess(result);
        return result;
    }

}
