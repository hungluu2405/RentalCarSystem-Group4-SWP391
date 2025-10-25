/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.implement.ContactDao;
import model.Contact;

public class ContactService {
    private ContactDao contactDao = new ContactDao();

    public boolean addContact(Contact contact) {
        try {
            // Kiểm tra dữ liệu (ví dụ cơ bản)
            if (contact.getName() == null || contact.getName().isEmpty()) return false;
            if (contact.getEmail() == null || contact.getEmail().isEmpty()) return false;
            if (contact.getMessage() == null || contact.getMessage().isEmpty()) return false;

            // Gọi DAO lưu vào DB
            contactDao.insertContact(contact);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
