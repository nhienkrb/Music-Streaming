package com.rhymthwave.Service_LR.Implement;

import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.DAO.RoleDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChangePasswordServiceImpl {

	private final AccountDAO dao;
}
