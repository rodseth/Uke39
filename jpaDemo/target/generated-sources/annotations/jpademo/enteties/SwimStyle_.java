package jpademo.enteties;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpademo.enteties.Person;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-09-23T11:05:01")
@StaticMetamodel(SwimStyle.class)
public class SwimStyle_ { 

    public static volatile ListAttribute<SwimStyle, Person> persons;
    public static volatile SingularAttribute<SwimStyle, Long> id;
    public static volatile SingularAttribute<SwimStyle, String> styleName;

}