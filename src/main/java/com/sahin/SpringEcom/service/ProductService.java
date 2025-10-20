package com.sahin.SpringEcom.service;

import com.sahin.SpringEcom.model.Product;
import com.sahin.SpringEcom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public class ProductService {

    @Autowired
    public ProductRepo productRepo;

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public Product getProductById(int id) {
        return productRepo.findById(id).orElse(new Product(-1));
    }

    public Product addOrUpdateProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());

        return productRepo.save(product);
    }



    //since Update and save Method is same we just use above code for both;
    //Updating and saving the data is the same thing;
//    public Product updatedProduct(Product product, MultipartFile image) throws IOException {
//        product.setImageName(image.getOriginalFilename());
//        product.setImageType(image.getContentType());
//        product.setImageData(image.getBytes());
//
//        return productRepo.save(product);
//    }


    public void deleteProduct(int id) {
        productRepo.deleteById(id);
    }
}
