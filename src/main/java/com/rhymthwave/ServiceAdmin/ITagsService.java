package com.rhymthwave.ServiceAdmin;

import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Tag;
import jakarta.servlet.http.HttpServletRequest;

public interface ITagsService extends CRUD<Tag,Long> {

    Tag save(Tag tag, HttpServletRequest request);
}
