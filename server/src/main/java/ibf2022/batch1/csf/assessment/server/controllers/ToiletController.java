package ibf2022.batch1.csf.assessment.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.batch1.csf.assessment.server.models.ToiletDto;
import ibf2022.batch1.csf.assessment.server.services.ToiletService;
import ibf2022.batch1.csf.assessment.server.utils.Utils;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path="/api/v1/toilets")
public class ToiletController {

    @Autowired
    private ToiletService toiletService;

    @GetMapping(path="/all")
    @CrossOrigin(origins="*")
    public ResponseEntity<String> getAllToilets() {
        List<ToiletDto> toiletDtoList = toiletService.getAllToilets();
        JsonArray jsonArray = Utils.toiletDtoListToJsonArray(toiletDtoList);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(jsonArray.toString());
    }

    @GetMapping(path="/{name}")
    public ResponseEntity<String> getToiletByName(@PathVariable String name) {
        ToiletDto toiletDto = toiletService.getToiletByName(name);
        JsonObject jsonObject = Utils.toiletDtoToJsonObject(toiletDto);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(jsonObject.toString());
    }
    
}
