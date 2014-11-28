/*******************************************************************************
 * Copyright (c) 2014 Jens Reimann.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package de.dentrassi.pm.aspect.common.osgi;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.jar.Manifest;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.google.gson.GsonBuilder;

import de.dentrassi.pm.aspect.ChannelAspect;
import de.dentrassi.pm.aspect.extract.Extractor;
import de.dentrassi.pm.osgi.bundle.BundleInformation;
import de.dentrassi.pm.osgi.bundle.BundleInformationParser;
import de.dentrassi.pm.osgi.feature.FeatureInformation;
import de.dentrassi.pm.osgi.feature.FeatureInformationParser;

public class OsgiExtractor implements Extractor
{
    public static final String KEY_CLASSIFIER = "classifier";

    public static final String KEY_MANIFEST = "manifest";

    public static final String KEY_VERSION = "version";

    public static final String KEY_NAME = "name";

    public static final String KEY_BUNDLE_INFORMATION = "bundle-information";

    public static final String KEY_FEATURE_INFORMATION = "feature-information";

    private final ChannelAspect aspect;

    public OsgiExtractor ( final ChannelAspect aspect )
    {
        this.aspect = aspect;
    }

    @Override
    public ChannelAspect getAspect ()
    {
        return this.aspect;
    }

    @Override
    public void extractMetaData ( final Path file, final Map<String, String> metadata ) throws Exception
    {
        extractBundleInformation ( file, metadata );
        extractFeatureInformation ( file, metadata );
    }

    private void extractFeatureInformation ( final Path file, final Map<String, String> metadata ) throws Exception
    {
        final FeatureInformation fi;
        try ( ZipFile zipFile = new ZipFile ( file.toFile () ) )
        {
            fi = new FeatureInformationParser ( zipFile ).parse ();
            if ( fi == null )
            {
                return;
            }
        }
        catch ( final ZipException e )
        {
            return;
        }

        metadata.put ( KEY_NAME, fi.getId () );
        metadata.put ( KEY_VERSION, fi.getVersion () );
        metadata.put ( KEY_CLASSIFIER, "eclipse.feature" );

        // store feature information

        final GsonBuilder gb = new GsonBuilder ();
        metadata.put ( KEY_FEATURE_INFORMATION, gb.create ().toJson ( fi ) );
    }

    private void extractBundleInformation ( final Path file, final Map<String, String> metadata ) throws Exception
    {
        final BundleInformation bi;
        try ( ZipFile zipFile = new ZipFile ( file.toFile () ) )
        {
            bi = new BundleInformationParser ( zipFile ).parse ();
            if ( bi == null )
            {
                return;
            }
        }
        catch ( final ZipException e )
        {
            return;
        }

        // store main attributes
        metadata.put ( KEY_NAME, bi.getId () );
        metadata.put ( KEY_VERSION, bi.getVersion () );
        metadata.put ( KEY_CLASSIFIER, "bundle" );

        // serialize manifest
        final ByteArrayOutputStream bos = new ByteArrayOutputStream ();
        final Manifest mf = new Manifest ();
        mf.write ( bos );
        bos.close ();
        metadata.put ( KEY_MANIFEST, bos.toString ( "UTF-8" ) );

        // store bundle information
        final GsonBuilder gb = new GsonBuilder ();
        metadata.put ( KEY_BUNDLE_INFORMATION, gb.create ().toJson ( bi ) );
    }

}
