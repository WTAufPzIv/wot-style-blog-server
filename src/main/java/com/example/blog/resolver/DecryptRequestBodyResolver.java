package com.example.blog.resolver;

import com.example.blog.annotation.DecryptRequestBody;
import com.example.blog.common.utils.RsaUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.validation.Validator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class DecryptRequestBodyResolver implements HandlerMethodArgumentResolver {
    static String privateKeyStr = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDGHh64wPlqxWLf\n" +
            "V7ZMBE7YezVvZja+YsqQU2n57V7uuSJ5edukQlxwLmS4RKXdjkDuxuOqukaltcRe\n" +
            "fg7ZWnnor92iJe9rN8xnS9MIl1tz5JsTNZTvWsJk1FID+ls8LewMyuV1m88YKtnR\n" +
            "SGdHT7591urZG892x8g7ABxor0N7cT33g5hKotzj1LHzrBv/RmWE5a+ARefcBw5K\n" +
            "otrIgYrHAD6/M6tFICdei6otSF0Ee/DM2YDVO6HSJYw3IbH/6hEGmNw7OSuZt7Zn\n" +
            "wDrPlIyXdtnCl3GY3ZMZAlK07SGZmuQc1x/yuC5UlNWUZW3z6uKk63sfCe9PphWt\n" +
            "xavB4oIPAgMBAAECggEAHQpUOYO76700QYXDaIAnKI9i5Unn0196zs4TVjnHtRl5\n" +
            "JrJIklrR3vXF+MbDQ2czOGz5+VAlw+uRdcJnnc+n2AZTaA3EewRqV2fjbrp1Atdr\n" +
            "LdUoWC+HGC2ROhkMH5WhqLxjByrIIARD+mvxC6ByTVLgCOQNNJ2TSaa6A1RRgXn1\n" +
            "Y7MGzrOUF2SfDShOGKW/poBAlGvGinpf0dulOdGsGA0GG/VII9UEHm0azAIgUpIo\n" +
            "CdFZLbA0ZgbyXzkUcPVoY+JCrkbx+UG2b21mgqlLgA8toOsPY60K7C6j29YP/kjm\n" +
            "0s9JVaUDXZGMqno/ArEGRioBheWbY08z9S2sJXa0PQKBgQDrSutPXbfABE9/uhqb\n" +
            "HKzZCT/wjRzh7VVuIFSN3al8ZJbT/8MFVTCd3uFyhjkxBeMOh1qMZ8N7Lob50MTV\n" +
            "Xy2Yt43HkAojqVO5348DyGerAFb5Arq5TpICjZdj6rPLCok0e2+MgcDL7nhsAVwP\n" +
            "59HbORGoZBiUsvNKssOnaayQXQKBgQDXjah13L7ogmQZnuq6x9Jy7wMNzGrAxcAg\n" +
            "vRnJgCivFT9f4CrdvIa+IZpGi9AnAYdz6CmBjsiB7l94esN3zUG6/Tpd847aT6ry\n" +
            "Vc3YCZDIWDqgzbN8fAKO34F59oeHnBb5EYaXUgtv674mZHhwbFM0L5qsnKqe9Gjx\n" +
            "Q7BDf4jlWwKBgQCY2ayPfGQ4GxTB9tX92dz3iSi+Tg1rP1B2IA6Nsme95WKOCeJY\n" +
            "/oR8qiNu0H8qt5ZPA+Lwvx+gUBNjCho0OoZWZTd3Yr0TeZZoczBm1rNxxOC7FujS\n" +
            "JJZ5jh3AzOFLj2aeb2mBr3Ddiec00pVJx+DuNt6fs5a8Upv19LdhdCakCQKBgQDA\n" +
            "uIvszS/3LFWZPqe51dzIHACL/r9XmbbczpXWvfoiCApAi2loFR4Xsm2AM5MovMpB\n" +
            "jEpH5sRIvzqmuoQ3gdIKAEyGIbQQZnh98M95H5xOM+w/R4r5etrCqPzXcd7dFWbI\n" +
            "y62db6KAL7M0GlJAmemOWB2fxPcb5YRKae2Xe9iGzwKBgHv2VYbCOBLtcOTINnsi\n" +
            "EPcxuqOrCW5yp8Q30A+m9vbeqVtXk0UVewTUSvfVkWMLyplzdkeRq1D+wQ4Li9xS\n" +
            "mIqyPeiR9iHMqLtMspFT52pleYfrZUh2EZsYGJA2brd8+Jk734AkkViQ3VnQudEo\n" +
            "hswMaAkz506iRieG/+uzP/bN\n" +
            "-----END PRIVATE KEY-----";

    private final ObjectMapper objectMapper;
    private final Validator validator; // 新增校验器

    public DecryptRequestBodyResolver(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DecryptRequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String body = new BufferedReader(new InputStreamReader(request.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
        JsonNode rootNode = objectMapper.readTree(body);
        decryptAllFields(rootNode);
        Object target = objectMapper.treeToValue(rootNode, parameter.getParameterType());
        // 手动执行校验
        if (parameter.hasParameterAnnotation(Valid.class)) {
            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "target");
            validator.validate(target, errors);
            if (errors.hasErrors()) {
                throw new MethodArgumentNotValidException(parameter, errors);
            }
        }
        return target;
    }

    private void decryptAllFields(JsonNode node) throws Exception {
        if (node.isObject()) {
            decryptObject((ObjectNode) node);
        } else if (node.isArray()) {
            decryptArray((ArrayNode) node);
        }
    }

    private void decryptObject(ObjectNode objectNode) throws Exception {
        Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode valueNode = entry.getValue();
            if (valueNode.isTextual()) {
                this.handleTextValue(objectNode, entry.getKey(), valueNode);
            } else {
                decryptAllFields(valueNode);
            }
        }
    }

    private void decryptArray(ArrayNode arrayNode) throws Exception {
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode element = arrayNode.get(i);
            if (element.isTextual()) {
                handleTextValue(arrayNode, i, element);
            } else {
                decryptAllFields(element);
            }
        }
    }

    private void handleTextValue(ObjectNode node,
                                 String key,
                                 JsonNode valueNode) throws Exception {
        String encrypted = valueNode.asText();
        String decrypted = RsaUtils.decrypt(encrypted, RsaUtils.loadPrivateKey(privateKeyStr));
        node.put(key, decrypted);
    }

    private void handleTextValue(ArrayNode arrayNode,
                                 int index,
                                 JsonNode valueNode) throws Exception {
        String encrypted = valueNode.asText();
        String decrypted = RsaUtils.decrypt(encrypted, RsaUtils.loadPrivateKey(privateKeyStr));
        arrayNode.set(index, decrypted);
    }
}
