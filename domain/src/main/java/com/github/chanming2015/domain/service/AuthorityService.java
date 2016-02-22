package com.github.chanming2015.domain.service;

import java.util.Set;


public interface AuthorityService {
	Set<String> getRoles(String userName);
	Set<String> getPermissions(String userName);
}
