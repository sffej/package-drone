package de.dentrassi.pm.osgi.tests;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.dentrassi.pm.osgi.FilterHelper;
import de.dentrassi.pm.osgi.FilterHelper.Multi;
import de.dentrassi.pm.osgi.FilterHelper.Pair;

public class FilterTest
{
    @Test
    public void test1 ()
    {
        Assert.assertEquals ( "(|(a=b)(c=d))", FilterHelper.or ( pairs ( "a", "b", "c", "d" ) ) );
        Assert.assertEquals ( "(a=b)", FilterHelper.or ( pairs ( "a", "b" ) ) );
        Assert.assertEquals ( "", FilterHelper.or ( pairs () ) );
    }

    @Test
    public void test2 ()
    {
        final Multi m = new FilterHelper.Multi ( "&" );
        final Multi m1 = new FilterHelper.Multi ( "|" );
        final Multi m2 = new FilterHelper.Multi ( "|" );

        m.addNode ( m1 );
        m.addNode ( m2 );

        m2.addNode ( new Pair ( "foo", "bar" ) );

        Assert.assertEquals ( "(foo=bar)", m.toString () );
    }

    @Test
    public void test3 ()
    {
        final Multi m = new FilterHelper.Multi ( "&" );
        final Multi m1 = new FilterHelper.Multi ( "|" );
        final Multi m2 = new FilterHelper.Multi ( "|" );

        m.addNode ( m1 );
        m.addNode ( m2 );

        m1.addNode ( new Pair ( "foo", "bar" ) );
        m2.addNode ( new Pair ( "foo2", "bar2" ) );

        Assert.assertEquals ( "(&(foo=bar)(foo2=bar2))", m.toString () );
    }

    @Test
    public void test4 ()
    {
        final Multi m = new FilterHelper.Multi ( "&" );
        final Multi m1 = new FilterHelper.Multi ( "|" );
        final Multi m2 = new FilterHelper.Multi ( "|" );

        m.addNode ( m1 );
        m.addNode ( m2 );

        Assert.assertEquals ( "", m.toString () );
    }

    @Test
    public void test5 ()
    {
        final Multi m = new FilterHelper.Multi ( "&" );
        final Multi m1 = new FilterHelper.Multi ( "|" );
        final Multi m2 = new FilterHelper.Multi ( "|" );

        m.addNode ( m1 );
        m.addNode ( m2 );

        m1.addNode ( new Pair ( "foo", "bar" ) );
        m1.addNode ( new Pair ( "answer", "42" ) );
        m2.addNode ( new Pair ( "foo2", "bar2" ) );
        m2.addNode ( new Pair ( "foz2", "baz2" ) );

        Assert.assertEquals ( "(&(|(foo=bar)(answer=42))(|(foo2=bar2)(foz2=baz2)))", m.toString () );
    }

    @Test
    public void test6 ()
    {
        final Multi m = new FilterHelper.Multi ( "&" );
        final Multi m1 = new FilterHelper.Multi ( "|" );
        final Multi m2 = new FilterHelper.Multi ( "|" );

        m.addNode ( m1 );
        m.addNode ( m2 );

        m1.addNode ( new Pair ( "foo", "bar" ) );
        m1.addNode ( new Pair ( "answer", "42" ) );

        Assert.assertEquals ( "(|(foo=bar)(answer=42))", m.toString () );
    }

    private List<Pair> pairs ( final String... tokens )
    {
        int pos = 0;

        final List<Pair> result = new LinkedList<> ();

        while ( pos + 1 < tokens.length )
        {
            result.add ( new Pair ( tokens[pos], tokens[pos + 1] ) );
            pos += 2;
        }

        return result;
    }
}
