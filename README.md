# Building Enterprise Websites with Liferay

## Enablement Bootcamp Prerequisite

Please review the course prerequisites [here](https://www.liferay.com/pt/web/events/enablement-bootcamp-prerequisites).

## Setting up environments

Here are some simple instructions to prepare your environment before the training starts.

1. Clone the repository to your computer.

    ```
    git clone https://github.com/liferay/enablement-bootcamp-clarity.git
    ```

1. Perform a git fetch, a day before the training starts, to ensure you have the latest version.

    ```
    git fetch origin
    ```

1. Checkout the exercise-01 branch.

    ```
    git checkout exercise-01
    ```

1. To ensure you have the most recent content for exercise-01 branch, run the following command.

    ```
    git pull origin exercise-01
    ```

1. In the terminal, navigate to the root folder of the repository and run the following command.

    ```
    cd enablement-bootcamp-clarity
    ```

    ```
    ./gradlew initBundle
    ```

    Or

    ```
    blade gw initBundle
    ```

1. After a successful build, run the 'blade server run' command to start your server.

    ```
    blade server run
    ```

    In case you don't have blade installed, you can start your server with Tomcat.

    ```
    cd bundles/tomcat/bin
    ```

    ```
    ./catalina.sh run
    ```

Now, a clean Liferay environment should have started.

After starting this environment, log in with the following credentials.

* Username: `admin@clarityvisionsolutions.com`
* Password: `learn`

Once logged in, access the Control Panel &rarr; Search &rarr; Index Actions &rarr; Execute Full Reindex.

And voilà! You're ready to dive into the bootcamp!

<!-- If you get curious and take a look at the repository, you'll notice it contains several branches named `exercise-xy`, being xy the exercise’s number. Those have been created to help you in case you get lost or need to go back to an earlier activity in the training. If that does happen, you can just follow the instructions previously described for the branch/exercise you need. -->


