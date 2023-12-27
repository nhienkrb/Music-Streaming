package com.rhymthwave.Service;

import com.rhymthwave.entity.UserType;

public interface UserTypeService {
	UserType generateEntity(String email, Integer subscriptionId,Integer paymentStatus);
}
