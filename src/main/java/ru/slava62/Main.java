package ru.slava62;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.slava62.db.dao.CategoriesMapper;
import ru.slava62.db.dao.ProductsMapper;
import ru.slava62.db.model.CategoriesExample;
import ru.slava62.db.model.Products;
import ru.slava62.db.model.ProductsExample;
import ru.slava62.util.DbUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        CategoriesMapper categoriesMapper = DbUtils.getCategoriesMapper();
        ProductsMapper productsMapper = DbUtils.getProductsMapper();
        CategoriesExample c= new CategoriesExample();
        ProductsExample p=new ProductsExample();
        p.createCriteria().andCategory_idEqualTo(1L);
        List<Products> prod=productsMapper.selectByExample(p);
        for (Products ps: prod
             ) {
            System.out.println("{\n id: " + ps.getId()
                    +"\ntitle: " + ps.getTitle()
                    +"\nprice: " + ps.getPrice()
                    +"\ncategiry_name: "+
                    categoriesMapper.selectByPrimaryKey(Math.toIntExact(ps.getCategory_id())).getTitle() +"\n}");
        }

    }
}
