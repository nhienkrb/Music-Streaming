package com.rhymthwave.Service;

import com.rhymthwave.entity.Author;

public interface AuthorService {
	Author findAuthor(Integer roleId, String email);
}
