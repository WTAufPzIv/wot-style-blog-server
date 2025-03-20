   package com.example.blog.config;

   import lombok.extern.slf4j.Slf4j;
   import org.springframework.http.HttpInputMessage;
   import org.springframework.http.HttpOutputMessage;
   import org.springframework.http.MediaType;
   import org.springframework.http.converter.AbstractHttpMessageConverter;
   import org.springframework.http.converter.HttpMessageNotReadableException;
   import org.springframework.http.converter.HttpMessageNotWritableException;
   import org.springframework.util.StreamUtils;
   import java.io.IOException;
   import java.nio.charset.StandardCharsets;
   import java.util.Map;

   @Slf4j
   public class PlainTextHttpMessageConverter extends AbstractHttpMessageConverter<Map<String, String>> {

       public PlainTextHttpMessageConverter() {
           super(new MediaType("text", "plain", StandardCharsets.UTF_8));
       }

       @Override
       protected boolean supports(Class<?> clazz) {
           return Map.class.isAssignableFrom(clazz);
       }

       @Override
       protected Map<String, String> readInternal(Class<? extends Map<String, String>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
           String body = StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
           String[] pairs = body.split("&");
           log.info(body);
           Map<String, String> result = new java.util.HashMap<>();
           for (String pair : pairs) {
               int idx = pair.indexOf("=");
               result.put(pair.substring(0, idx), pair.substring(idx + 1));
           }
           return result;
       }

       @Override
       protected void writeInternal(Map<String, String> t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
           // 这里不需要实现，因为我们只处理请求
       }
   }
