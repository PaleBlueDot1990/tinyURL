package com.bhuvnesh.tinyURL.Repository;

import com.bhuvnesh.tinyURL.Model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface IUrlRepository extends JpaRepository<Url,Long>
{
    public Url findByShortLink(String shortLink);
}
