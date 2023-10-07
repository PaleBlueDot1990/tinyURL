package com.bhuvnesh.tinyURL.Controller;

import com.bhuvnesh.tinyURL.Model.Url;
import com.bhuvnesh.tinyURL.Model.UrlDto;
import com.bhuvnesh.tinyURL.Model.UrlErrorResponseDto;
import com.bhuvnesh.tinyURL.Model.UrlResponseDto;
import com.bhuvnesh.tinyURL.Service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class UrlController
{
    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto)
    {
        System.out.println(urlDto.getUrl());
        System.out.println(urlDto.getExpirationDate());

        Url urlToReturn = this.urlService.generateShortLink(urlDto);

        if(urlToReturn != null)
        {
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlDto.getUrl());
            urlResponseDto.setShortLink(urlToReturn.getShortLink());
            urlResponseDto.setExpirationDate(urlToReturn.getExpirationDate());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
        }

        UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
        urlErrorResponseDto.setStatus("404");
        urlErrorResponseDto.setErrorMessage("There was an error processing your request. Please try again.");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<UrlErrorResponseDto> redirectToOriginalUrl(@PathVariable String shortLink,
                                                                     HttpServletResponse response) throws IOException
    {
        if(shortLink.isEmpty())
        {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setErrorMessage("Short link is empty");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
        }

        Url urlToReturn = this.urlService.getEncodedUrl(shortLink);
        if(urlToReturn == null)
        {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setErrorMessage("The url for this short link does not exit, or it might have expired");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
        }

        if(urlToReturn.getExpirationDate().isBefore(LocalDateTime.now()))
        {
            this.urlService.deleteShortLink(urlToReturn);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setErrorMessage("This short link has expired. Please generate a fresh one.");
            urlErrorResponseDto.setStatus("200");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
        }

        response.sendRedirect(urlToReturn.getOriginalUrl());
        return null;
    }
}
