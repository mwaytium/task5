package com.nc.JAXBmodel;

import com.nc.entity.UserGroupEntity;

import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.List;

@Stateless
public class JAXB {
    private static final String FILE_PATH_XML = "test.xml";

    public void marshall(List<UserGroupEntity> groups) {
        try {
            JAXBContext jaxbc = JAXBContext.newInstance(UserListGroupWrapper.class);
            Marshaller marshaller = jaxbc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            File file = null;
            FileOutputStream os = null;
            try {
                UserListGroupWrapper wrapper = new UserListGroupWrapper();
                wrapper.setGroups(groups);
                file = new File(FILE_PATH_XML);
                file.createNewFile();
                os = new FileOutputStream(file, false);
                marshaller.marshal(wrapper, file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public UserListGroupWrapper unmarshall() {
        UserListGroupWrapper wrapper = null;
        try {
            JAXBContext context = JAXBContext.newInstance(UserListGroupWrapper.class);
            Unmarshaller marshaller = context.createUnmarshaller();
            File file = null;
            FileInputStream is = null;
            try {
                file = new File(FILE_PATH_XML);
                file.createNewFile();
                is = new FileInputStream(file);
                wrapper = (UserListGroupWrapper) marshaller.unmarshal(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return wrapper;
    }
}
