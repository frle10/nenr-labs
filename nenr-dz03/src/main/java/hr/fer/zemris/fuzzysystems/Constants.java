package hr.fer.zemris.fuzzysystems;

import hr.fer.zemris.fuzzy.IDomain;

import static java.lang.Math.*;
import static hr.fer.zemris.fuzzy.Domain.intRange;

/**
 * 
 * 
 * @author Ivan Skorupan
 */
public class Constants {
	
	public static final int MAX_DISTANCE = 1300;
	
	public static final int MAX_WIND_VELOCITY = 50;
	
	public static final int MAX_ACCELERATION = 35;
	
	public static final int MAX_ANGLE = 90;
	
	public static final int SHIP_SIZE = 10;
	
	public static final IDomain DISTANCE_DOMAIN = intRange(0, MAX_DISTANCE + 1);
	
	public static final IDomain ANGLE_DOMAIN = intRange(-MAX_ANGLE, MAX_ANGLE + 1);
	
	public static final IDomain VELOCITY_DOMAIN = intRange(0, (int)round(sqrt(2 * MAX_DISTANCE * MAX_ACCELERATION) + MAX_WIND_VELOCITY));
	
	public static final IDomain ACCELERATION_DOMAIN = intRange(-MAX_ACCELERATION, MAX_ACCELERATION + 1);
	
	public static final IDomain DIRECTION_DOMAIN = intRange(0, 2);
	
	public static final ConclusionEngine MINIMUM_ENGINE = (v, p, e) -> min(v, p.getValueAt(e));
	
	public static final ConclusionEngine PRODUCT_ENGINE = (v, p, e) -> v * p.getValueAt(e);
	
}
