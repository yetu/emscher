package com.yetu.emscher.mantarepo;

import com.codahale.metrics.health.HealthCheck;
import com.yetu.emscher.app.config.MantaConfig;

public class MantaHealthCheck extends HealthCheck {

	private MantaConfig config;

	public MantaHealthCheck(MantaConfig config) {
		this.config = config;
	}

	@Override
	protected Result check() throws Exception {

		return Result.healthy();
	}

}
