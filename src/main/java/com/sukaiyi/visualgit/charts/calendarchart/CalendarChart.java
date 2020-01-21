package com.sukaiyi.visualgit.charts.calendarchart;

import com.google.gson.Gson;
import com.sukaiyi.visualgit.charts.Chart;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import com.sukaiyi.visualgit.utils.DateUtils;

import java.util.*;
import java.util.stream.Collectors;

public class CalendarChart implements Chart {

    private static Gson gson = new Gson();

    private String author;

    public CalendarChart() {

    }

    public CalendarChart(String author) {
        this.author = author;
    }

    @Override
    public Object data(List<GitCommitInfo> commitInfos) {
        if (author != null) {
            commitInfos = commitInfos.stream().filter(info -> Objects.equals(info.getAuthor(), author)).collect(Collectors.toList());
        }
        Map<String, List<GitCommitInfo>> groupedByDate = commitInfos.stream()
                .collect(Collectors.groupingBy(info -> DateUtils.format(new Date(info.getTimestamp()), "yyyy-MM-dd")));
        List<List<Object>> data = groupedByDate.entrySet().stream().map(entry -> Arrays.<Object>asList(entry.getKey(), entry.getValue().size())).collect(Collectors.toList());

        List<String> orderedDate = groupedByDate.keySet().stream().sorted().collect(Collectors.toList());
        String minDate = orderedDate.get(0);
        String maxDate = orderedDate.get(orderedDate.size() - 1);

        int minMonth = Integer.parseInt(minDate.substring(5, 7));
        String startDate = minMonth < 6 ? minDate.substring(0, 4) + "-01-01" : minDate.substring(0, 4) + "-07-01";
        int maxMonth = Integer.parseInt(maxDate.substring(5, 7));
        String endDate = maxMonth < 6 ? maxDate.substring(0, 4) + "-06-30" : maxDate.substring(0, 4) + "-12-31";

        Map<String, Object> retData = new HashMap<>();
        retData.put("data", gson.toJson(data));
        retData.put("max", groupedByDate.values().stream().mapToInt(List::size).max().orElse(1));
        retData.put("min", 1);
        retData.put("segments", splitDate(startDate, endDate));
        return retData;
    }

    private List<List<String>> splitDate(String startDate, String endDate) {
        Calendar start = parseCalendar(startDate);
        Calendar end = parseCalendar(endDate);
        List<List<String>> segments = new ArrayList<>();
        while (start.before(end)) {
            List<String> segment = new ArrayList<>(2);
            segment.add(DateUtils.format(start.getTime(), "yyyy-MM-dd"));
            start.set(Calendar.MONTH, start.get(Calendar.MONTH) + 5);
            start.set(Calendar.DAY_OF_MONTH, start.getActualMaximum(Calendar.DAY_OF_MONTH));
            segment.add(DateUtils.format(start.getTime(), "yyyy-MM-dd"));
            start.set(Calendar.MONTH, start.get(Calendar.MONTH) + 1);
            start.set(Calendar.DAY_OF_MONTH, start.getActualMinimum(Calendar.DAY_OF_MONTH));
            segments.add(segment);
        }
        return segments;
    }

    private Calendar parseCalendar(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }
}
