package com.filecloud.uiservice.client.endpoint;

import com.filecloud.uiservice.client.request.DocumentUpdateRequest;
import com.filecloud.uiservice.client.request.IdRequest;
import com.filecloud.uiservice.client.request.ShareDocumentRequest;
import com.filecloud.uiservice.client.response.DocumentResponse;
import com.filecloud.uiservice.client.response.SingleFieldResponse;
import com.filecloud.uiservice.client.response.SpaceInfoResponse;
import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.response.Result;
import feign.Headers;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@FeignClient(contextId = "DocumentService", name = "GatewayServer", path = UiConst.URL_DOCUMENT_SERVICE)
public interface DocumentServiceClient {

    @PostMapping("/share")
    Result<String> share(@RequestHeader("Authorization") String bearerToken, @RequestBody ShareDocumentRequest request);

    @PostMapping("/delete")
    Result<String> delete(@RequestHeader("Authorization") String bearerToken, @RequestBody IdRequest request);

    @PostMapping("/update")
    Result<DocumentResponse> update(@RequestHeader("Authorization") String bearerToken, @RequestBody DocumentUpdateRequest request);

    @GetMapping("/download/{documentId}")
    Response download(@RequestHeader("Authorization") String bearerToken, @PathVariable long documentId);

    @Headers("Content-Type: multipart/form-data; boundary=<calculated when request is sent>")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<DocumentResponse> upload(@RequestHeader("Authorization") String bearerToken, @RequestPart("document") MultipartFile document, @RequestPart("properties") String properties);

    @GetMapping("/{documentId}")
    Result<DocumentResponse> getDocument(@RequestHeader("Authorization") String bearerToken, @PathVariable long documentId);

    @GetMapping("/list-active-documents")
    Result<List<DocumentResponse>> listActiveDocuments(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/space-info")
    Result<SpaceInfoResponse> spaceInfo(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/count-active-documents")
    Result<SingleFieldResponse> countActiveDocuments(@RequestHeader("Authorization") String bearerToken);

    @PostMapping("/recycle")
    Result<String> recycleDocument(@RequestHeader("Authorization") String bearerToken, @RequestBody IdRequest request);

    @PostMapping("/restore")
    Result<String> restoreDocument(@RequestHeader("Authorization") String bearerToken, @RequestBody IdRequest request);

    @GetMapping("/list-recycled-documents")
    Result<List<DocumentResponse>> listRecycledDocuments(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/count-recycled-documents")
    Result<SingleFieldResponse> countRecycledDocuments(@RequestHeader("Authorization") String bearerToken);

}
