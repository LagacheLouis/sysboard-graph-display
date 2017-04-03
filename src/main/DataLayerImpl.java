package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import cern.mpe.systems.core.domain.SystemUnderTest;
import cern.mpe.systems.core.domain.relation.SystemRelation;
import cern.mpe.systems.core.service.control.SystemRelationControllerImpl;
import cern.mpe.systems.core.service.control.SystemsControllerImpl;
import cern.mpe.systems.core.service.manage.SynchronizedMultiMapSystemRelationEngine;
import cern.mpe.systems.core.service.manage.SystemAttributesManagerImpl;
import cern.mpe.systems.core.service.manage.SystemRelationManagerImpl;
import cern.mpe.systems.core.service.manage.SystemsManagerImpl;
import cern.mpe.systems.core.service.provider.SystemRelationProvider;
import cern.mpe.systems.core.service.provider.SystemsProvider;
import datacore.AbstractDataLayer;
import debugprovider.RandomGenRelations;
import debugprovider.RandomGenSystems;
import filters.Filter;
import json.JSONExporter;

public class DataLayerImpl extends AbstractDataLayer{

	private static DataLayerImpl instance;
	
	private DataLayerImpl() {
		super();
	}
	
	public final static DataLayerImpl getInstance() {

        if (DataLayerImpl.instance == null) {
           synchronized(DataLayerImpl.class) {
             if (DataLayerImpl.instance == null) {
               DataLayerImpl.instance = new DataLayerImpl();
               DataLayerImpl.getInstance().isFirstLaunch = true;
             }
           }
        }
        return DataLayerImpl.instance;
    }
	
	//setup of systemsManager, systems providers, etc
	@Override
	protected void setUpSystems()
	{
		systemsManager = new SystemsManagerImpl();
		systemProviders = new ArrayList<SystemsProvider>();
		RandomGenSystems test = new RandomGenSystems();
		systemProviders.add(test);
		systemsManager.setSystemsProviders(systemProviders);
        SystemAttributesManagerImpl systemAttributesManager = new SystemAttributesManagerImpl();
        systemController = new SystemsControllerImpl();
        systemController.setSystemsManager(systemsManager);
        systemController.setSystemAttributesManager(systemAttributesManager);
        systemsManager.init();
	}
	
	@Override
	protected void setUpSystemRelations(){
		relationManager = new SystemRelationManagerImpl();
		relationProviders = new ArrayList<SystemRelationProvider>();
		relationEngine = new SynchronizedMultiMapSystemRelationEngine();
		
		RandomGenRelations randomRelationProvider = new RandomGenRelations();
		randomRelationProvider.genAllRelations(systemsManager);
		relationProviders.add(randomRelationProvider);
		relationManager.setSystemRelationEngine(relationEngine);
		relationManager.setSystemsManager(systemsManager);
		relationManager.setSystemRelationProviders(relationProviders);
		relationManager.init();
		relationController = new SystemRelationControllerImpl();
		relationController.setSystemRelationManager(relationManager);
		relationController.init();
	}
	
	@Override
	protected void setUpFilters(){
		filters = new ArrayList<Filter>();
	}

	@Override
	protected Collection<SystemRelation> convertToDisplayList(Collection<SystemUnderTest> listSystemsUnderTest)
	{
		Collection<SystemRelation> relToDisplay = new HashSet<SystemRelation>();
		for(SystemRelationProvider provider : relationProviders){
			relToDisplay.addAll(provider.getAllSystemRelations(systemsManager));
		}
		return relToDisplay;
	}
	
}
