package com.is2.tp3.config;

import org.hibernate.envers.RevisionListener;

import com.is2.tp3.audit.Revision;

public class CustomRevisionListener implements RevisionListener{
	public void newRevision(Object revisionEntity) {
		final Revision revision = (Revision) revisionEntity;
	}
}
