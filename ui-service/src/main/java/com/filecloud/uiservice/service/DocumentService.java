package com.filecloud.uiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.filecloud.uiservice.client.endpoint.DocumentServiceClient;
import com.filecloud.uiservice.client.request.DocumentUpdateRequest;
import com.filecloud.uiservice.client.request.IdRequest;
import com.filecloud.uiservice.client.response.DocumentResponse;
import com.filecloud.uiservice.client.response.SingleFieldResponse;
import com.filecloud.uiservice.client.response.SpaceInfoResponse;
import com.filecloud.uiservice.dto.mvcmodel.DocumentModel;
import com.filecloud.uiservice.dto.mvcmodel.DocumentUploadModel;
import com.filecloud.uiservice.dto.mvcmodel.DownloadDocumentModel;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.util.FileUtil;
import feign.Response;
import org.bouncycastle.crypto.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DocumentService extends BaseService {

    private final DocumentServiceClient documentServiceClient;

    private final FileUtil fileUtil;

    private final ObjectMapper objectMapper;

    @Autowired
    public DocumentService(DocumentServiceClient documentServiceClient, ObjectMapper objectMapper, FileUtil fileUtil) {
        this.documentServiceClient = documentServiceClient;
        this.fileUtil = fileUtil;
        this.objectMapper = objectMapper;
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

    public Result<DocumentResponse> upload(String bearerToken, DocumentUploadModel model) {
        MultipartFile requestPartFile = null;

        try {
            requestPartFile = fileUtil.getEncryptedMultipartFile(model.getDocument());
        } catch (IOException | CryptoException e) {
            error(e.getMessage());
        }

        ObjectNode request = objectMapper.createObjectNode();
        request.put("name", model.getName());
        request.put("description", model.getDescription());

        Result<DocumentResponse> result = documentServiceClient.upload(bearerToken, requestPartFile, request.toString());
        throwIfInvalidAccess(result);
        return result;
    }

    public DownloadDocumentModel download(String bearerToken, long documentId) {
        Response response = documentServiceClient.download(bearerToken, documentId);

        if (response.status() != HttpStatus.OK.value() || response.body() == null)
            return null;

        try {
            DownloadDocumentModel model = new DownloadDocumentModel();
            model.setHeadersMap(response.headers());
            model.setResource(fileUtil.decryptResponse(response));
            return model;
        } catch (IOException | CryptoException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Result<DocumentResponse> update(String bearerToken, DocumentUpdateRequest request) {
        Result<DocumentResponse> result = documentServiceClient.update(bearerToken, request);
        throwIfInvalidAccess(result);
        return result;
    }

    public Result<String> delete(String bearerToken, long id) {
        Result<String> result = documentServiceClient.delete(bearerToken, new IdRequest(id));
        throwIfInvalidAccess(result);
        return result;
    }
}
