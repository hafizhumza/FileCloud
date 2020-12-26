package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.DocumentServiceClient;
import com.filecloud.uiservice.client.response.SingleFieldResponse;
import com.filecloud.uiservice.client.response.SpaceInfoResponse;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DocumentService extends BaseService {

    private final DocumentServiceClient documentServiceClient;

    @Autowired
    public DocumentService(DocumentServiceClient documentServiceClient) {
        this.documentServiceClient = documentServiceClient;
    }

    public String count(String bearerToken) {
        Result<SingleFieldResponse> result = documentServiceClient.count(bearerToken);
        logIfError(result);
        return result.getData().getResponse().toString();
    }

    public SpaceInfoResponse spaceInfo(String bearerToken) {
        Result<SpaceInfoResponse> result = documentServiceClient.spaceInfo(bearerToken);
        logIfError(result);
        SpaceInfoResponse response = result.getData();

        if (response == null)
            return new SpaceInfoResponse();

        response.setUsedSpace(Util.roundUptoTwo(response.getUsedSpace()));
        response.setRemainingSpace(Util.roundUptoTwo(response.getRemainingSpace()));
        response.setSpaceLimit(Util.roundUptoTwo(response.getSpaceLimit()));
        return response;
    }

}
