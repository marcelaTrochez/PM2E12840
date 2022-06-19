package com.aplicacion.pm2e12840.Tables;

public class Contacts
  {
      private Integer id;
      private String country;
      private String name;
      private String phone;
      private String note;

      public Contacts() {
        }

      public Contacts(Integer id, String country, String name, String phone, String note)
      {
          this.id = id;
          this.country = country;
          this.name = name;
          this.phone = phone;
          this.note = note;
      }

        public Integer getId() {

          return id;
        }

        public void setId(Integer id) {

          this.id = id;
        }

        public String getCountry() {

          return country;
        }

        public void setCountry(String country) {

          this.country = country;
        }

        public String getName() {

          return name;
        }

        public void setName(String name) {

          this.name = name;
        }

        public String getPhone() {

          return phone;
        }

        public void setPhone(String phone) {

          this.phone = phone;
        }

        public String getNote() {

          return note;
        }

        public void setNote(String note) {

          this.note = note;
        }

    }
