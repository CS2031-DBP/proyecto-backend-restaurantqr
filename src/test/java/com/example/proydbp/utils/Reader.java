package com.example.proydbp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class Reader {

    @Autowired
    private ObjectMapper mapper;  // Inyección de ObjectMapper de Jackson

    // Método para leer un archivo JSON desde el classpath
    public static String readJsonFile(String filePath) throws IOException {
        // Localiza el archivo en el classpath usando ClassPathResource
        File resource = new ClassPathResource(filePath).getFile();
        // Lee el contenido del archivo en un array de bytes y lo convierte en String
        byte[] byteArray = Files.readAllBytes(resource.toPath());
        return new String(byteArray);
    }

    // Método para actualizar el valor de una clave en un archivo JSON
    public String updateId(String json, String key, Long id) throws JsonProcessingException {
        // Lee el JSON como un árbol de nodos
        JsonNode jsonNode = mapper.readTree(json);
        // Actualiza el valor de la clave proporcionada
        ((ObjectNode) jsonNode).put(key, id);
        // Convierte el JSON actualizado a String
        return mapper.writeValueAsString(jsonNode);
    }
}


