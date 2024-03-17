# USE CASE: Produce a Report on Population by Continent, Region, and Country

## CHARACTERISTIC INFORMATION

### Goal in Context

As a user, I want to produce reports on the population of continents, regions, and countries, so that I can analyze demographic trends and make informed decisions for the organization.

### Scope

System (black-box)

### Level

Primary task.

### Preconditions

Database contains up-to-date and comprehensive population data for continents, regions, and countries.

### Success End Condition

A detailed report on the population of the specified geographic area is generated and made available to the user.

### Failed End Condition

The system fails to produce the requested population report.

### Primary Actor

User.

### Trigger

The user requests population data for a continent, region, or country from the database.

## MAIN SUCCESS SCENARIO

1. The user initiates a request for a population report.
2. The user specifies the geographic level of interest: continent, region, or country.
3. Based on the user’s selection, the system queries the database for the corresponding population data.
4. The user accesses the queried population data.
5. A report is generated, providing detailed population information for the chosen geographic area, organized in a user-friendly format.

## EXTENSIONS

None.

## SUB-VARIATIONS

-   Continent Population: Detailed population report for a specified continent.
-   Region Population: Detailed population report for a specified region.
-   Country Population: Detailed population report for a specified country.

## SCHEDULE

**DUE DATE**: Release 0.1.9
