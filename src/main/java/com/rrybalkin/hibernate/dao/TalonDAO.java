package com.rrybalkin.hibernate.dao;

import com.rrybalkin.hibernate.entity.Talon;

public interface TalonDAO extends EntityDAO<Talon> {

  void resetAllTalons() throws Exception;

  void resetBreakfastById(long talonId) throws Exception;

  void resetLunchById(long talonId) throws Exception;

  void resetDinnerById(long talonId) throws Exception;

  int getCountBreakfasts() throws Exception;

  int getCountLunches() throws Exception;

  int getCountDinners() throws Exception;
}
