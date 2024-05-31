package application.DutyRoster;

import java.util.Objects;

/**
 * Employee为immutable类型数据
 */
public class Employee {
    private final String name;
    private final String jobTitle;
    private final String phoneNumber;
    // Abstraction function:
    // 构建一个员工类有名字、职务、电话
    // Representation invariant:
    // 名字、职务电话号码不为空
    // Safety from rep exposure:
    //  使用private和final来确保其为immutable
    public Employee(String name, String jobTitle, String phoneNumber) {
        if(Objects.equals(name, "")){
            throw new IllegalArgumentException("名字不能为空");
        }
        if(Objects.equals(jobTitle, "")){
            throw new IllegalArgumentException("职务不能为空");
        }if(Objects.equals(phoneNumber, "")){
            throw new IllegalArgumentException("电话号码不能为空");
        }
        this.name = name;
        this.jobTitle = jobTitle;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    @Override
    public String toString(){
        return this.name+"    "+this.jobTitle+"    "+this.phoneNumber;
    }
}
