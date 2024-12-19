public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("CLEANING CHECKLIST PROGRAM");

        // 1. Getting the cleaner's name and room information
        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            // 1. Getting the cleaner's name and room information
            String cleanerName;
            while (true) {
                try {
                    System.out.print("Enter name of the cleaner: ");
                    cleanerName = scanner.nextLine().trim();
                    if (cleanerName.isEmpty() || !cleanerName.matches("^[a-zA-Z\\s]+$")) {
                        throw new IllegalArgumentException("Name must contain only letters and spaces. Please try again.");
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            
            int maxRoomNumber;
            while (true) {
                try {
                    System.out.print("Room number lim~it: ");
                    maxRoomNumber = scanner.nextInt();
                    if (maxRoomNumber <= 0) {
                        throw new IllegalArgumentException("Room number limit must be a positive integer.");
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input. Please enter a positive integer.");
                    scanner.nextLine(); // Clear invalid input
                }
            }
            
            java.util.List<Integer> roomNumbers = new java.util.ArrayList<>();
            java.util.Map<Integer, String> roomInfo = new java.util.HashMap<>();
            
            for (int i = 0; i < maxRoomNumber; i++) {
                int roomNumber;
                while (true) {
                    try {
                        System.out.print("Enter room number: ");
                        roomNumber = scanner.nextInt();
                        if (roomNumber <= 0) {
                            throw new IllegalArgumentException("Room number must be a positive integer.");
                        }
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid input. Please enter a positive integer.");
                        scanner.nextLine(); // Clear invalid input
                    }
                }
                
                scanner.nextLine(); // Consume the leftover newline
                String roomSize;
                while (true) {
                    try {
                        System.out.print("Enter size of room " + roomNumber + " (big/small): ");
                        roomSize = scanner.nextLine().toLowerCase().trim();
                        if (!roomSize.equals("big") && !roomSize.equals("small")) {
                            throw new IllegalArgumentException("Invalid room size. Please enter 'big' or 'small'.");
                        }
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                
                roomNumbers.add(roomNumber);
                roomInfo.put(roomNumber, roomSize);
            }
            
            // 2. Storing unchecked tasks for each room
            java.util.Map<Integer, java.util.List<String>> uncheckedTasksPerRoom = new java.util.HashMap<>();
            
            // 3. Looping through each room to check tasks
            for (int roomNumber : roomNumbers) {
                System.out.println("\nChecking tasks for room " + roomNumber + " (" + roomInfo.get(roomNumber) + " room):");
                java.util.List<String> uncheckedTasks = new java.util.ArrayList<>();
                
                // Dusting checklist
                java.util.List<String> dustingChecklist = java.util.Arrays.asList("Floor", "Bathroom", "Under the Bed", "Wooden Racks");
                System.out.println("\nDusting Checklist:");
                for (String task : dustingChecklist) {
                    while (true) {
                        try {
                            if (!checkTask(scanner, "dusting " + task)) {
                                uncheckedTasks.add("Dusting - " + task + " not completed");
                            }
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                
                // Arrangement checklist
                java.util.List<String> arrangementChecklist = java.util.Arrays.asList("Bedsheets", "Pillows", "Blankets");
                System.out.println("\nArrangement Checklist:");
                for (String task : arrangementChecklist) {
                    while (true) {
                        try {
                            if (!checkTask(scanner, "arranging " + task)) {
                                uncheckedTasks.add("Arrangement - " + task + " not completed");
                            }
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                
                // Room-specific tasks based on size
                System.out.println("\nRoom Specific Checklist:");
                java.util.Map<String, java.util.List<Item>> roomSizeChecklist = getRoomSizeChecklist();
                java.util.List<Item> checklist = roomSizeChecklist.get(roomInfo.get(roomNumber));
                if (checklist == null) {
                    System.out.println("Invalid room size! Skipping this room.");
                    continue;
                }
                
                for (Item item : checklist) {
                    while (true) {
                        try {
                            int missing = checkQuantityTask(scanner, item.name, item.quantity);
                            if (missing > 0) {
                                uncheckedTasks.add(item.name + " - " + missing + " required");
                            }
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                
                // Store unchecked tasks for this room
                if (!uncheckedTasks.isEmpty()) {
                    uncheckedTasksPerRoom.put(roomNumber, uncheckedTasks);
                }
            }
            
            // 4. Summary of unchecked tasks
            System.out.println("\n" + cleanerName + " has completed checking " + roomNumbers + ".");
            if (!uncheckedTasksPerRoom.isEmpty()) {
                System.out.println("\n" + cleanerName + " did not finish the following tasks in each room:");
                for (java.util.Map.Entry<Integer, java.util.List<String>> entry : uncheckedTasksPerRoom.entrySet()) {
                    System.out.println("Room " + entry.getKey() + " (" + roomInfo.get(entry.getKey()) + " room):");
                    for (String task : entry.getValue()) {
                        System.out.println("- " + task);
                    }
                }
            } else {
                System.out.println("\n" + cleanerName + " has completed all tasks in all rooms!");
            }
        }
    }

    // Helper class for items
    static class Item {
        String name;
        int quantity;

        Item(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }

    // Helper function to get room size checklist
    private static java.util.Map<String, java.util.List<Item>> getRoomSizeChecklist() {
        java.util.Map<String, java.util.List<Item>> roomSizeChecklist = new java.util.HashMap<>();
        roomSizeChecklist.put("big", java.util.Arrays.asList(
                new Item("Big Towels", 5),
                new Item("Hand Towels", 5),
                new Item("Foot Towel", 1),
                new Item("Bathroom Glasses", 4),
                new Item("Pillows", 5),
                new Item("Blankets", 5),
                new Item("Bed Sheets", 3)
        ));
        roomSizeChecklist.put("small", java.util.Arrays.asList(
                new Item("Big Towels", 2),
                new Item("Hand Towels", 2),
                new Item("Foot Towel", 1),
                new Item("Bathroom Glasses", 2),
                new Item("Pillows", 2),
                new Item("Blankets", 2),
                new Item("Bed Sheet", 1)
        ));
        return roomSizeChecklist;
    }

    // Helper function to check if a task is completed (yes/no)
    private static boolean checkTask(java.util.Scanner scanner, String task) {
        while (true) {
            System.out.print("Is '" + task + "' done? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            switch (response) {
                case "yes", "y" -> {
                    return true;
                }
                case "no", "n" -> {
                    return false;
                }
                default -> System.out.println("Invalid input. Please enter 'yes', 'y', 'no', 'n'.");
            }
        }
    }

    // Helper function to check the quantity of items
    private static int checkQuantityTask(java.util.Scanner scanner, String task, int requiredQuantity) {
        while (true) {
            try {
                System.out.print("How many '" + task + "' are present? (Required: " + requiredQuantity + "): ");
                int availableQuantity = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline
                if (availableQuantity < 0) {
                    throw new IllegalArgumentException("Quantity cannot be negative.");
                } else if (availableQuantity > requiredQuantity) {
                    throw new IllegalArgumentException("Too many items! Required: " + requiredQuantity);
                } else if (availableQuantity < requiredQuantity) {
                    return requiredQuantity - availableQuantity;
                }
                return 0;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please enter a valid quantity.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
