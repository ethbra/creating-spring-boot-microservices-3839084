package com.example.explorecalijpa.web;

import java.util.NoSuchElementException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.explorecalijpa.business.TourRatingService;
import com.example.explorecalijpa.model.TourRating;

import jakarta.validation.Valid;

/**
 * Tour Rating Controller
 *
 * Created by Mary Ellen Bowman & Ethan Brassfield <3
 */
@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {
  private TourRatingService tourRatingService;

  public TourRatingController(TourRatingService tourRatingService) {
    this.tourRatingService = tourRatingService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createTourRating(@PathVariable(value = "tourId") int tourId,
      @RequestBody @Valid RatingDto ratingDto) {
    tourRatingService.createNew(tourId, ratingDto.getCustomerId(), ratingDto.getScore(), ratingDto.getComment());
  }

  // Out: All ratings for data transfer object TourRating
  @GetMapping
  public List<RatingDto> getAllRatingsForTour(@PathVariable(value = "tourId") int tourID) {
    List<TourRating> tourRatings = tourRatingService.lookupRatings(tourID);
    return tourRatings.stream().map(RatingDto::new).toList();
  }

  // In: Tour ID Out: average of all ratings
  @GetMapping("/average")
  public Map<String, Double> getAverage(@PathVariable(value = "tourId") int tourID) {
    return Map.of("average", tourRatingService.getAverageScore(tourID));
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String return404(NoSuchElementException e) {
    return e.getMessage();
  }

}
