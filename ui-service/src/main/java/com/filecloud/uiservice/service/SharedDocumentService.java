package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.SharedDocumentClient;
import com.filecloud.uiservice.dto.mvcmodel.DownloadDocumentModel;
import com.filecloud.uiservice.util.FileUtil;
import feign.Response;
import org.bouncycastle.crypto.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class SharedDocumentService extends BaseService {

    private final SharedDocumentClient sharedDocumentClient;

    private final FileUtil fileUtil;

    @Autowired
    public SharedDocumentService(SharedDocumentClient sharedDocumentClient, FileUtil fileUtil) {
        this.sharedDocumentClient = sharedDocumentClient;
        this.fileUtil = fileUtil;
    }

    public DownloadDocumentModel downloadSharedDocument(String token) {
        Response response = sharedDocumentClient.downloadSharedDocument(token);

        if (response.status() != HttpStatus.OK.value() || response.body() == null)
            notFound();

        try {
            DownloadDocumentModel model = new DownloadDocumentModel();
            model.setHeadersMap(response.headers());
            model.setResource(fileUtil.decryptResponse(response));
            return model;
        } catch (IOException | CryptoException e) {
            error();
        }

        return null;
    }
}
