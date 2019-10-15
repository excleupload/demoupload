package com.example.tapp.business.service;

import java.util.HashMap;

public interface DashboardService {

	HashMap<String, Long> counts();

	HashMap<String, Object> getUserData(Integer year);

}
