package com.example.tapp.business.service.implementation;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tapp.business.service.DashboardService;
import com.example.tapp.data.dao.UserConnectionDao;
import com.example.tapp.data.dao.UserDao;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserDao userDao;

    @Autowired
    public UserConnectionDao connDao;

    @Override
    public HashMap<String, Long> counts() {
        HashMap<String, Long> counts = new HashMap<>();
        counts.put("totalUser", userDao.getUsersCount());
        counts.put("totalMaleUser", userDao.getMaleUserCount());
        counts.put("totalFemaleUser", userDao.getFemaleUserCount());
        counts.put("totalConnection", connDao.getTotalConnection() / 2);
        return counts;
    }

    @Override
    public HashMap<String, Object> getUserData(Integer year) {
        List<Object[]> data = userDao.getUserCountByYear(year);
        Object row[][] = new Object[12][2];
        long max = 10L;
        for (int i = 0; i < 12; i++) {
            row[i][0] = getMonthName(i + 1);
            final int temp = i + 1;
            Optional<Object[]> op = data.stream().filter(obj -> obj[1].equals(temp)).findFirst();
            row[i][1] = op.isPresent() ? op.get()[0] : 0L;
            max = (max < (long) row[i][1]) ? (long) row[i][1] : max;
        }

        HashMap<String, Object> barChartData = new HashMap<>();
        barChartData.put("data", row);
        barChartData.put("max", max % 2 == 0 ? max + 2 : max + 3);
        return barChartData;
    }

    public String getMonthName(int num) {
        switch (num) {
        case 1:
            return "Jan";
        case 2:
            return "Feb";
        case 3:
            return "Mar";
        case 4:
            return "Apr";
        case 5:
            return "May";
        case 6:
            return "June";
        case 7:
            return "July";
        case 8:
            return "Aug";
        case 9:
            return "Sept";
        case 10:
            return "Oct";
        case 11:
            return "Nev";
        case 12:
            return "Dec";
        default:
            return "N/A";
        }
    }
}