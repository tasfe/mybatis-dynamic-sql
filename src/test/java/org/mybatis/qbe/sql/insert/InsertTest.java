package org.mybatis.qbe.sql.insert;

import static org.mybatis.qbe.sql.insert.render.InsertSupportShortcut.*;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.sql.JDBCType;

import org.junit.Test;
import org.mybatis.qbe.mybatis3.MyBatis3Field;
import org.mybatis.qbe.sql.insert.render.InsertValuesRenderer;
import org.mybatis.qbe.sql.insert.render.InsertSupport;
import org.mybatis.qbe.sql.where.SqlField;

public class InsertTest {

    @Test
    public void testInsertClauseRenderer() {
        SqlField<Integer> id = SqlField.of("id", JDBCType.INTEGER);
        SqlField<String> firstName = SqlField.of("firstName", JDBCType.VARCHAR);
        SqlField<String> lastName = SqlField.of("lastName", JDBCType.VARCHAR);
        SqlField<String> occupation = SqlField.of("occupation", JDBCType.VARCHAR);
        
        InsertValues insertValues = new InsertValues.Builder(firstName, "fred")
                .andValue(lastName, "jones")
                .andValue(id, 3)
                .andValue(occupation, "dino driver")
                .build();
        
        InsertSupport insertSupport = InsertValuesRenderer.of(insertValues).render();

        String expectedFieldsPhrase = "(firstName, lastName, id, occupation)";
        assertThat(insertSupport.getFieldsPhrase(), is(expectedFieldsPhrase));

        String expectedValuesPhrase = "values (?, ?, ?, ?)";
        assertThat(insertSupport.getValuesPhrase(), is(expectedValuesPhrase));
        
        assertThat(insertSupport.getParameters().size(), is(4));
        assertThat(insertSupport.getParameters().get("p1"), is("fred"));
        assertThat(insertSupport.getParameters().get("p2"), is("jones"));
        assertThat(insertSupport.getParameters().get("p3"), is(3));
        assertThat(insertSupport.getParameters().get("p4"), is("dino driver"));
    }

    @Test
    public void testInsertClauseRendererStartWithNull() {
        SqlField<Integer> id = SqlField.of("id", JDBCType.INTEGER);
        SqlField<String> firstName = SqlField.of("firstName", JDBCType.VARCHAR);
        SqlField<String> lastName = SqlField.of("lastName", JDBCType.VARCHAR);
        SqlField<String> occupation = SqlField.of("occupation", JDBCType.VARCHAR);
        
        InsertValues insertValues = new InsertValues.Builder(firstName)
                .andValue(lastName, "jones")
                .andValue(id, 3)
                .andValue(occupation, "dino driver")
                .build();
        
        InsertSupport insertSupport = InsertValuesRenderer.of(insertValues).render();

        String expectedFieldsPhrase = "(firstName, lastName, id, occupation)";
        assertThat(insertSupport.getFieldsPhrase(), is(expectedFieldsPhrase));

        String expectedValuesPhrase = "values (?, ?, ?, ?)";
        assertThat(insertSupport.getValuesPhrase(), is(expectedValuesPhrase));
        
        assertThat(insertSupport.getParameters().size(), is(4));
        assertThat(insertSupport.getParameters().get("p1"), is(nullValue()));
        assertThat(insertSupport.getParameters().get("p2"), is("jones"));
        assertThat(insertSupport.getParameters().get("p3"), is(3));
        assertThat(insertSupport.getParameters().get("p4"), is("dino driver"));
    }

    @Test
    public void testInsertClauseRendererForMyBatis() {
        MyBatis3Field<Integer> id = MyBatis3Field.of("id", JDBCType.INTEGER);
        MyBatis3Field<String> firstName = MyBatis3Field.of("firstName", JDBCType.VARCHAR);
        MyBatis3Field<String> lastName = MyBatis3Field.of("lastName", JDBCType.VARCHAR);
        MyBatis3Field<String> occupation = MyBatis3Field.of("occupation", JDBCType.VARCHAR);
        
        InsertValues insertValues = new InsertValues.Builder(firstName, "fred")
                .andNullValue(lastName)
                .andValue(id, 3)
                .andValue(occupation, "dino driver")
                .build();
        
        InsertSupport insertSupport = InsertValuesRenderer.of(insertValues).render();

        String expectedFieldsPhrase = "(firstName, lastName, id, occupation)";
        assertThat(insertSupport.getFieldsPhrase(), is(expectedFieldsPhrase));

        String expectedValuesPhrase = "values (#{parameters.p1,jdbcType=VARCHAR}, "
                + "#{parameters.p2,jdbcType=VARCHAR}, "
                + "#{parameters.p3,jdbcType=INTEGER}, "
                + "#{parameters.p4,jdbcType=VARCHAR})";
        assertThat(insertSupport.getValuesPhrase(), is(expectedValuesPhrase));
        
        assertThat(insertSupport.getParameters().size(), is(4));
        assertThat(insertSupport.getParameters().get("p1"), is("fred"));
        assertThat(insertSupport.getParameters().get("p2"), is(nullValue()));
        assertThat(insertSupport.getParameters().get("p3"), is(3));
        assertThat(insertSupport.getParameters().get("p4"), is("dino driver"));
    }
    
    @Test
    public void testInsertClauseShortcut() {
        MyBatis3Field<Integer> id = MyBatis3Field.of("id", JDBCType.INTEGER);
        MyBatis3Field<String> firstName = MyBatis3Field.of("firstName", JDBCType.VARCHAR);
        MyBatis3Field<String> lastName = MyBatis3Field.of("lastName", JDBCType.VARCHAR);
        MyBatis3Field<String> occupation = MyBatis3Field.of("occupation", JDBCType.VARCHAR);
        
        InsertSupport insertSupport = insertValue(firstName, "fred")
                .andValue(lastName, "jones")
                .andNullValue(id)
                .andValue(occupation, "dino driver")
                .render();
        
        String expectedFieldsPhrase = "(firstName, lastName, id, occupation)";
        assertThat(insertSupport.getFieldsPhrase(), is(expectedFieldsPhrase));

        String expectedValuesPhrase = "values (#{parameters.p1,jdbcType=VARCHAR}, "
                + "#{parameters.p2,jdbcType=VARCHAR}, "
                + "#{parameters.p3,jdbcType=INTEGER}, "
                + "#{parameters.p4,jdbcType=VARCHAR})";
        assertThat(insertSupport.getValuesPhrase(), is(expectedValuesPhrase));
        
        assertThat(insertSupport.getParameters().size(), is(4));
        assertThat(insertSupport.getParameters().get("p1"), is("fred"));
        assertThat(insertSupport.getParameters().get("p2"), is("jones"));
        assertThat(insertSupport.getParameters().get("p3"), is(nullValue()));
        assertThat(insertSupport.getParameters().get("p4"), is("dino driver"));
    }

    @Test
    public void testInsertClauseShortcutStartWithNull() {
        MyBatis3Field<Integer> id = MyBatis3Field.of("id", JDBCType.INTEGER);
        MyBatis3Field<String> firstName = MyBatis3Field.of("firstName", JDBCType.VARCHAR);
        MyBatis3Field<String> lastName = MyBatis3Field.of("lastName", JDBCType.VARCHAR);
        MyBatis3Field<String> occupation = MyBatis3Field.of("occupation", JDBCType.VARCHAR);
        
        InsertSupport insertSupport = insertNullValue(firstName)
                .andValue(lastName, "jones")
                .andNullValue(id)
                .andValue(occupation, "dino driver")
                .render();
        
        String expectedFieldsPhrase = "(firstName, lastName, id, occupation)";
        assertThat(insertSupport.getFieldsPhrase(), is(expectedFieldsPhrase));

        String expectedValuesPhrase = "values (#{parameters.p1,jdbcType=VARCHAR}, "
                + "#{parameters.p2,jdbcType=VARCHAR}, "
                + "#{parameters.p3,jdbcType=INTEGER}, "
                + "#{parameters.p4,jdbcType=VARCHAR})";
        assertThat(insertSupport.getValuesPhrase(), is(expectedValuesPhrase));
        
        assertThat(insertSupport.getParameters().size(), is(4));
        assertThat(insertSupport.getParameters().get("p1"), is(nullValue()));
        assertThat(insertSupport.getParameters().get("p2"), is("jones"));
        assertThat(insertSupport.getParameters().get("p3"), is(nullValue()));
        assertThat(insertSupport.getParameters().get("p4"), is("dino driver"));
    }
}