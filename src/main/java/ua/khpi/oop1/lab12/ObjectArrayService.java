package ua.khpi.oop1.lab12;


final class ObjectArrayService {
    private ObjectArrayService() {
    }

    private static void checkRange(Device[] data, int size) {
        if (data == null || size < 0 || size > data.length) {
            throw new IllegalArgumentException("нелогічний розмір");
        }
    }

    static int indexOf(Device[] data, int size, int id) {
        checkRange(data, size);
         for (int index = 0; index < size; index++) {
             if (data[index].id() == id) {
                 return index;
                 }
             }
         return -1;
         }


    static Device find(Device[] data, int size, int id) {
        int index = indexOf(data, size, id);
        return index < 0 ? null : data[index];
    }


    static double totalPower(Device[] data, int size) {
        checkRange(data, size);
        double sum = 0.0;
        for (int index = 0; index < size; index++) {
            sum += data[index].power();
        }
        return sum;
    }


    static int add(Device[] data, int size, Device device) {
        checkRange(data, size);
        if (device == null || size == data.length) {
            return size;
        }
        if (indexOf(data, size, device.id()) >= 0) {
            return size;
        }
        data[size] = device;
        return size + 1;
    }



    static int removeAt(Device[] data, int size, int id) {
        checkRange(data, size);
        int index = indexOf(data, size, id);
        if (index < 0) {
            return size;
        }
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        return size - 1;
    }

    static int removeDuplicates(Device[] data, int size) {
        return removeDuplicatesTraced(data, size, null);
    }


    static int removeDuplicatesTraced(Device[] data, int size, StringBuilder trace) {
        checkRange(data, size);
        int unique = 0;
        for (int read = 0; read < size; read++) {
            Device candidate = data[read];
            boolean seen = false;
            for (int index = 0; index < unique; index++) {
                if (data[index].id() == candidate.id()) {
                    seen = true;
                    break;
                }
            }
            if (!seen) {
                data[unique] = candidate;
                unique++;
            }
            if (trace != null) {
                trace.append("read=").append(read)
                        .append(" id=").append(candidate.id())
                        .append(seen ? " дублікат, пропуск" : " унікальний, запис")
                        .append(" | unique=").append(unique)
                        .append(" | ").append(ids(data)).append('\n');
            }
        }
        for (int index = unique; index < size; index++) {
            data[index] = null;
        }
        if (trace != null) {
            trace.append("очищення хвоста | size=").append(unique)
                    .append(" | ").append(ids(data)).append('\n');
        }
        return unique;
    }


    static String ids(Device[] data) {
        StringBuilder sb = new StringBuilder("[");
        for (int index = 0; index < data.length; index++) {
            if (index > 0) {
                sb.append(", ");
            }
            if (data[index] == null) {
                sb.append("null");
            } else {
                sb.append(data[index].id());
            }
        }
        return sb.append(']').toString();
    }
}