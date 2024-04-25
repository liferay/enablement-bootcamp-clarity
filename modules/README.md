## Getting Started

1. Build and deploy the **site-initializer-api**, **site-initializer-service** and **clarity-site-initializer** modules

2. Log in as the administrator and configure the site initializer under *Control Panel > System Settings > Liferay Enablement Bootcamp (section) > Clarity Site Initializer*


## How does this work?

1. The site-initializer-api module contains the contracts, which basically amounts to the SiteInitializerTask. A task is something that you want the initializer to do (e.g. Add Users, create Roles, associate Users to Roles, etc).

2. The site-initializer-service module contains the definitions of the tasks. Each “activity” is stored in its own task so that we maintain discreet services that are properly encapsulated.

3. The clarity-site-initializer is an OSGI based site initializer. The main entry point for the initializer does three things.

   a. Builds a context object (which you should modify if you need additional things passed to the SiteInitializerTask you write)

   b. Loads the data from the **site-initializer/** folder under resources

   c. The data is placed into the context object using the file name (minus the extension) as the key for the map. e.g. users.json goes into the map with USERS as the key and the string of content as the value

   d. Uses a configuration to load the tasks. The list is order based on how they are ordered in the configuration

   e. Loop over the configured task list and execute each task, passing the groupId and context object (which should contain everything you need)

4. Configuration is required for this to work. Go to Control Panel. > System Settings > Liferay Enablement Bootcamp (section) > Clarity Site Initializer

5. Once there you can enable the initializer and whichever tasks you need to run. You can re order the tasks by dragging and dropping the blocks.  This is using Liferay’s configuration API but is managing the view through custom JSP. The list of services will be based on a service tracker lookup so make sure you include the @Component(service = SiteInitializerTask.class) to the services that you define.


## How to use this framework?

1. Navigate to **/modules/site-initializer-service/src/main/java/com/liferay/site/initializer/task/impl**

2. Create a new class (or better yet, copy an existing one)

3. Name your class using a standard naming convention — *[Model Name]SiteInitializerTask*