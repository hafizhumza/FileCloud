package com.filecloud.uiservice.client.endpoint;

import com.filecloud.uiservice.constant.UiConst;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(contextId = "SharedDocuments", name = "GatewayServer", path = UiConst.URL_DOCUMENT_SERVICE)
public interface SharedDocumentClient {

    @GetMapping("shared/{token}")
    Response downloadSharedDocument(@PathVariable String token);

}
