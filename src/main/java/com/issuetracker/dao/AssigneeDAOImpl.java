package com.issuetracker.dao;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.issuetracker.model.Assignee;
import com.issuetracker.model.Unit;

public class AssigneeDAOImpl implements AssigneeDAO
{
    private List<Assignee> assigneeList = new ArrayList<>();

    public AssigneeDAOImpl()
    {
	 Assignee assigneeOne = new Assignee("MTI-A-ADM-001", "Carry Luke",
	 "carry.luke", Unit.ADMINISTRATION, 3);

	 Assignee assigneeTwo = new Assignee("MTI-A-ADM-002", "Rodrick Luther",
	 "rodrick_luther",
	 Unit.ADMINISTRATION, 3);

	 Assignee assigneeThree = new Assignee("MTI-A-CSG-001", "Miki Worth",
	 "miki.w", Unit.CONSIGNMENT, 1);

	 Assignee assigneeFour = new Assignee("MTI-A-CSG-002", "Carlena Fife",
	 "c_fife", Unit.CONSIGNMENT, 0);

	 Assignee assigneeFive = new Assignee("MTI-A-CSG-003", "Cedrick Padgett",
	 "c.padgett", Unit.CONSIGNMENT, 0);

	 Assignee assigneeSix = new Assignee("MTI-A-CSG-004", "Tyrell Eaves",
	 "tyrell_e", Unit.CONSIGNMENT, 0);

	 Assignee assigneeSeven = new Assignee("MTI-A-CSG-005", "Jewel Seaton",
	 "jewel_seaton", Unit.CONSIGNMENT, 0);

	 Assignee assigneeEight = new Assignee("MTI-A-PAY-001", "Larita Conklin",
	 "larita.conklin", Unit.PAYMENT, 1);

	 Assignee assigneeNine = new Assignee("MTI-A-PAY-002", "Elyse Chu",
	 "elyse.chu", Unit.PAYMENT, 1);

	 Assignee assigneeTen = new Assignee("MTI-A-SHP-001", "Elane Lester",
	 "elane_lester", Unit.SHIPMENT, 1);

	 Assignee assigneeEleven = new Assignee("MTI-A-SHP-002",
	 "Valery Champion", "valery_c",
	 Unit.SHIPMENT, 0);

	 Assignee assigneeTwelve = new Assignee("MTI-A-SHP-003", "Aaron Godfrey",
	 "aaron.g", Unit.SHIPMENT, 0);

	 Assignee assigneeThirteen = new Assignee("MTI-A-SHP-004", "Jarvis Ivy",
	 "jarvis_ivy", Unit.SHIPMENT, 0);

	 Assignee assigneeFourteen = new Assignee("MTI-A-SHP-005",
	 "Zackary Marble", "zackary.m",
	 Unit.SHIPMENT, 0);

	 Assignee assigneeFifteen = new Assignee("MTI-A-SHP-006",
	 "Williams Weir",
	 "williams_weir", Unit.SHIPMENT, 0);

	 assigneeList = List.of(assigneeOne, assigneeTwo, assigneeThree,
	 assigneeFour, assigneeFive, assigneeSix,
	 assigneeSeven, assigneeEight, assigneeNine,
	 assigneeTen, assigneeEleven, assigneeTwelve,
	 assigneeThirteen, assigneeFourteen,
	 assigneeFifteen);
    }

    @Override
    public List<Assignee> fetchAssignees(Unit unit)
    {
	Map<Unit, String> issueTypeCodeMap = new EnumMap<>(Unit.class);
	issueTypeCodeMap.put(Unit.ADMINISTRATION, "ADM");
	issueTypeCodeMap.put(Unit.CONSIGNMENT, "CSG");
	issueTypeCodeMap.put(Unit.PAYMENT, "PAY");
	issueTypeCodeMap.put(Unit.SHIPMENT, "SHP");

	String issueCode = issueTypeCodeMap.get(unit);

	 return assigneeList.stream()
	 .filter(assignee -> issueCode.equals(assignee.getAssigneeId()
	 .split("-")[2]))
	 .collect(Collectors.toList());
    }

    @Override
    public Assignee getAssigneeByEmail(String assigneeEmail)
    {
	 return assigneeList.parallelStream()
	 .filter(assignee ->
	 assigneeEmail.equals(assignee.getAssigneeEmail()))
	 .findFirst()
	 .orElse(null);
    }
}