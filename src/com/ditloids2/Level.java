package com.ditloids2;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.Context;

import java.util.*;

public class Level {

    // ������ ���������
    private ArrayList<String> ditloids = null;

    // ������ ���������
    private ArrayList<String> hints = null;
    
    private int levelIndex = 0;

    // ����������� ������� ���������� �� ������ � ������� levelIndex �� �������� ��������� context
    // � ����������� ���������� ���������� � settings
    public Level(Context context, int _levelIndex){
        levelIndex = _levelIndex;
        Resources res = context.getResources();
        String[] levels = res.getStringArray(R.array.levels);
        String[] mashints = res.getStringArray(R.array.hints);
        ditloids = new ArrayList<String>();
        hints = new ArrayList<String>();
        String str = Integer.toString(levelIndex);
        for(int i = 0; i < levels.length; i++){
            String[] parts = levels[i].split("_");
            if(parts[1].equals(str)){
                ditloids.add(parts[0]);
            }
        }
        for(int i = 0; i < mashints.length; i++){
            String[] parts = mashints[i].split("_");
            if(parts[1].equals(str)){
                hints.add(parts[0]);
            }
        }
    }

    // ��������� ����� ������������  probablyAnswer �� ������� � �������� ditloidIndex
    public boolean Verify(int ditloidIndex, String probablyAnswer){
        if(ditloidIndex > ditloids.size() || ditloidIndex < 0 ) return false;
        if(ditloids.get(ditloidIndex).toLowerCase().equals(probablyAnswer.toLowerCase())){
            return true;
        }else
            return false;
    }

    // �������� ������� � �������� ditloidIndex
    public String GetDitloid(int ditloidIndex){
        String ditloid = "";
        if(ditloidIndex < ditloids.size() && ditloidIndex > -1){
            String[] str = ditloids.get(ditloidIndex).trim().split(" ");
            for(int i=0; i<str.length; i++)
            	if(str[i].trim().substring(0, 1).matches("[0-9]"))
            		ditloid = ditloid + " " + str[i].trim();
            	else
            	{
            		if(Character.isUpperCase(str[i].trim().charAt(0)))
            			ditloid = ditloid + " " + str[i].trim().substring(0,1);
            		else
            			ditloid = ditloid + " " + str[i].trim();
            	}
            return ditloid.trim();
        }
        else
            return "";
    }

    // �������� ����� �� ������� � �������� ditloidIndex, ���� �� ��� ������� (����� ���������)
    public String GetDitloidAnswer(int ditloidIndex){
        if(ditloidIndex > ditloids.size() || ditloidIndex < 0) return "��������� ���� �� ����������";
        return ditloids.get(ditloidIndex);
    }

    // �������� ��������� �� ������� � �������� ditloidIndex, ���� �� ��� ������� (����� ������ ������)
    public String GetDitloidHint(int ditloidIndex){
        if(ditloidIndex > hints.size() || ditloidIndex < 0 || hints == null) return "��������� ���-�� ����� ;)";
        return hints.get(ditloidIndex);
    }

    
    // ����� ���������� ���������
    public int GetDitloidsCount(){
        return ditloids.size();
    }
    
    public int GetLevelIndex(){
        return levelIndex;
    }
}
