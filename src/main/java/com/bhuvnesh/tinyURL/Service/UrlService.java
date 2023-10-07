package com.bhuvnesh.tinyURL.Service;

import com.bhuvnesh.tinyURL.Model.Url;
import com.bhuvnesh.tinyURL.Model.UrlDto;
import com.bhuvnesh.tinyURL.Repository.IUrlRepository;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class UrlService implements IUrlService
{
    @Autowired
    private IUrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto urlDto)
    {
        if(!urlDto.getUrl().isEmpty())
        {
            String encodedUrl = this.encodeUrl(urlDto.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setCreationDate(LocalDateTime.now());
            urlToPersist.setExpirationDate(this.getExpirationDate(urlDto.getExpirationDate(),
                                                                  urlToPersist.getCreationDate()));
            Url persistedUrl = this.persistUrl(urlToPersist);
            return persistedUrl;
        }
        return null;
    }

    @Override
    public String encodeUrl(String url)
    {
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.adler32()
                            .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                            .toString();
        return encodedUrl;
    }

    @Override
    public LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate)
    {
        if(expirationDate.isEmpty())
        {
            return creationDate.plusSeconds(300);
        }
        return LocalDateTime.parse(expirationDate);
    }

    @Override
    public Url persistUrl(Url url)
    {
        Url persistedUrl = this.urlRepository.save(url);
        return persistedUrl;
    }

    @Override
    public Url getEncodedUrl(String url)
    {
        Url urlToReturn = this.urlRepository.findByShortLink(url);
        return urlToReturn;
    }

    @Override
    public void deleteShortLink(Url url)
    {
        this.urlRepository.delete(url);
    }
}
