package de.dentrassi.pm.storage.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-02-04T15:11:44.119+0100")
@StaticMetamodel(ChannelEntity.class)
public class ChannelEntity_ {
	public static volatile SingularAttribute<ChannelEntity, String> id;
	public static volatile SingularAttribute<ChannelEntity, String> name;
	public static volatile SetAttribute<ChannelEntity, ArtifactEntity> artifacts;
	public static volatile ListAttribute<ChannelEntity, String> aspects;
	public static volatile CollectionAttribute<ChannelEntity, ExtractedChannelPropertyEntity> extractedProperties;
	public static volatile CollectionAttribute<ChannelEntity, ProvidedChannelPropertyEntity> providedProperties;
	public static volatile SetAttribute<ChannelEntity, DeployGroupEntity> deployGroups;
}
