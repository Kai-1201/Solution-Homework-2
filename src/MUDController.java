import java.util.Scanner;

public class MUDController {
    private  Player player;
    private boolean running;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
    }


    public void runGameLoop() {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to the MUD game! Type 'help' for commands.");
        while (running) {
            System.out.print("> ");
            String input = in.nextLine();
            handleInput(input);
        }
        System.out.println("The game is over. See you next time!");
    }


    public void handleInput(String input) {
        if (input.isEmpty()) return;
        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String argument;
        if (parts.length > 1) argument = parts[1];
        else argument = "";


        if (command.equals("look")) {
            lookAround();
        } else if (command.equals("move")) {
            move(argument);
        } else if (command.equals("pick")) {
            pickUp(argument);
        } else if (command.equals("inventory")) {
            checkInventory();
        } else if (command.equals("help")) {
            showHelp();
        } else if (command.equals("quit") || command.equals("exit")) {
            running = false;
        } else {
            System.out.println("Unknown command. Type 'help' for a list of commands.");
        }

    }

    private void lookAround() {
        Room currentRoom = player.getCurrentRoom();
        System.out.println("You are in: " + currentRoom.getDescription());
        if (currentRoom.getItems().isEmpty()) System.out.println("There are no items here.");
        else System.out.println("Items in the room: " + currentRoom.getItems());
    }

    private void move(String direction) {
        if (direction.isEmpty()) {
            System.out.println("Please specify a direction (forward, back, left, right).");
            return;
        }
        Room nextRoom = player.getCurrentRoom().getNeighbor(direction);
        if (nextRoom == null) System.out.println("You cannot go in that direction.");
        else {
            player.setCurrentRoom(nextRoom);
            System.out.println("You moved: " + direction);
            lookAround();
        }
    }



    private void pickUp(String argument) {
        if (argument.isEmpty()) {
            System.out.println("Specify the name of the item to pick up.");
            return;
        }
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.findItem(argument);
        if (item ==null) System.out.println("There is no item with the name: " + argument);
        else {
            player.addItem(item);
            currentRoom.removeItem(item);
            System.out.println("You picked up: " + item.getName());
        }
    }



    private void checkInventory() {
        if (player.getInventory().isEmpty()) System.out.println("Your inventory is empty.");
        else  System.out.println("Your inventory: " + player.getInventory());


    }


    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("help - list of commands");
        System.out.println("move <forward|back|left|right> - move in a direction");
        System.out.println("look - inspect the room");
        System.out.println("pick up <itemName> - pick up an item");
        System.out.println("inventory - check your inventory");
        System.out.println("quit/exit - exit the game");

    }
}



class Main {
    public static void main(String[] args) {
        Room dungeon = new Room("Dungeon", "A dark and cold room with chains hanging from the walls.");
        Room corridor = new Room("Corridor", "A narrow stone passage with flickering torches along the walls.");
        Room armory = new Room("Armory", "A room filled with rusty armor and old, unused weapons.");
        dungeon.setNeighbor("forward", corridor);
        corridor.setNeighbor("back", dungeon);
        corridor.setNeighbor("right", armory);
        armory.setNeighbor("left", corridor);
        dungeon.addItem(new Item("Dagger", "A small, sharp dagger with a cracked handle."));
        corridor.addItem(new Item("Helmet", "An old, dented helmet, offering little protection."));
        armory.addItem(new Item("Sword", "A well-worn sword, slightly rusted but still sharp."));
        Player player = new Player(dungeon);
        MUDController controller = new MUDController(player);
        controller.runGameLoop();

    }
}