package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.DocumentServiceClient;
import com.filecloud.uiservice.client.response.DocumentResponse;
import com.filecloud.uiservice.client.response.SingleFieldResponse;
import com.filecloud.uiservice.client.response.SpaceInfoResponse;
import com.filecloud.uiservice.dto.mvcmodel.DocumentModel;
import com.filecloud.uiservice.dto.mvcmodel.SpaceInfoModel;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.util.Util;
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
        throwIfInvalidAccessOrInternalError(result);
        return result.getData().getResponse().toString();
    }

    public SpaceInfoModel spaceInfo(String bearerToken) {
        Result<SpaceInfoResponse> result = documentServiceClient.spaceInfo(bearerToken);
        throwIfInvalidAccessOrInternalError(result);
        SpaceInfoResponse response = result.getData();

        if (response == null)
            return new SpaceInfoModel();

        return new SpaceInfoModel(
                Util.humanReadableByteCountBin(response.getSpaceLimit()),
                Util.humanReadableByteCountBin(response.getUsedSpace()),
                Util.humanReadableByteCountBin(response.getRemainingSpace())
        );
    }

    public List<DocumentModel> listDocuments(String bearerToken) {
        Result<List<DocumentResponse>> result = documentServiceClient.listDocuments(bearerToken);
        throwIfInvalidAccessOrInternalError(result);
        return result.getData().stream().map(DocumentModel::new).collect(Collectors.toList());
    }

}
