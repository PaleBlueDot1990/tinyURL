package com.bhuvnesh.tinyURL.Service;

import com.bhuvnesh.tinyURL.Model.Url;
import com.bhuvnesh.tinyURL.Model.UrlDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface IUrlService
{
    public Url generateShortLink(UrlDto urlDto);
    public Url persistUrl(Url url);
    public Url getEncodedUrl(String url);
    public void deleteShortLink(Url url);
    public String encodeUrl(String url);
    LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate);
}
