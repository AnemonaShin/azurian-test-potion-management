package cl.management.potion.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Rest controller for the potion part on the solution.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
@RestController
@RequestMapping(path = "/entidades/potions")
public class PotionController {

  @Tag(name = "registers", description = "Data registers endpoints")
  @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public String createPotion(@RequestBody String entity) {

    return entity;
  }

  @Tag(name = "searchs", description = "Searchs endpoints")
  @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<Object> searchPotions(@RequestParam String param) {
    return null;
  }

}
