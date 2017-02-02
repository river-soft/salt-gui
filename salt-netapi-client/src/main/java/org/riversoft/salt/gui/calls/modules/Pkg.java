package org.riversoft.salt.gui.calls.modules;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.riversoft.salt.gui.calls.LocalCall;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * salt.modules.pkg
 */
public class Pkg {

    /**
     * Information about a package as returned by "pkg.search".
     */
    public static class PackageInfo {

        private String summary;

        public String getSummary() {
            return summary;
        }
    }

    /**
     * Package dictionary as returned by "pkg.file_dict".
     */
    public static class PackageDict {

        private List<Object> errors;
        private Map<String, List<String>> packages;

        public List<Object> getErrors() {
            return errors;
        }

        public Map<String, List<String>> getPackages() {
            return packages;
        }
    }

    /**
     * Information about a package as returned by pkg.info_installed and
     * pkg.info_available
     */
    public static class Info {

        @SerializedName("arch")
        private Optional<String> architecture = Optional.empty();
        @SerializedName("build_date")
        private Optional<ZonedDateTime> buildDate = Optional.empty();
        @SerializedName("build_date_time_t")
        private Optional<Long> buildDateUnixTime = Optional.empty();
        @SerializedName("build_host")
        private Optional<String> buildHost = Optional.empty();
        private Optional<String> description = Optional.empty();
        private Optional<String> group = Optional.empty();
        @SerializedName("install_date")
        private Optional<ZonedDateTime> installDate = Optional.empty();
        @SerializedName("install_date_time_t")
        private Optional<Long> installDateUnixTime = Optional.empty();
        private Optional<String> license = Optional.empty();
        @SerializedName("new_features_have_been_added")
        private Optional<String> newFeaturesHaveBeenAdded = Optional.empty();
        private Optional<String> packager = Optional.empty();
        private Optional<String> release = Optional.empty();
        private Optional<String> relocations = Optional.empty();
        private Optional<String> signature = Optional.empty();
        private Optional<String> size = Optional.empty();
        private Optional<String> source = Optional.empty();
        private Optional<String> summary = Optional.empty();
        private Optional<String> url = Optional.empty();
        private Optional<String> vendor = Optional.empty();
        private Optional<String> version = Optional.empty();
        private Optional<String> epoch = Optional.empty();

        public Optional<String> getArchitecture() {
            return architecture;
        }

        public Optional<ZonedDateTime> getBuildDate() {
            return buildDate;
        }

        public Optional<String> getBuildHost() {
            return buildHost;
        }

        public Optional<String> getGroup() {
            return group;
        }

        public Optional<String> getDescription() {
            return description;
        }

        public Optional<ZonedDateTime> getInstallDate() {
            return installDate;
        }

        public Optional<String> getLicense() {
            return license;
        }

        public Optional<String> getNewFeaturesHaveBeenAdded() {
            return newFeaturesHaveBeenAdded;
        }

        public Optional<String> getPackager() {
            return packager;
        }

        public Optional<String> getRelease() {
            return release;
        }

        public Optional<String> getRelocations() {
            return relocations;
        }

        public Optional<String> getSignature() {
            return signature;
        }

        public Optional<String> getSize() {
            return size;
        }

        public Optional<String> getSource() {
            return source;
        }

        public Optional<String> getSummary() {
            return summary;
        }

        public Optional<String> getUrl() {
            return url;
        }

        public Optional<String> getVendor() {
            return vendor;
        }

        public Optional<String> getVersion() {
            return version;
        }

        public Optional<String> getEpoch() {
            return epoch;
        }

        public Optional<Long> getBuildDateUnixTime() {
            return buildDateUnixTime;
        }

        public Optional<Long> getInstallDateUnixTime() {
            return installDateUnixTime;
        }
    }

    private Pkg() { }

    public static LocalCall<Map<String, PackageInfo>> search(String criteria) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("criteria", criteria);
        return new LocalCall<>("pkg.search", Optional.empty(), Optional.of(args),
                new TypeToken<Map<String, PackageInfo>>(){});
    }

    public static LocalCall<PackageDict> fileDict(String... packages) {
        return new LocalCall<>("pkg.file_dict", Optional.of(Arrays.asList(packages)),
                Optional.empty(), new TypeToken<PackageDict>(){});
    }

    public static LocalCall<Map<String, List<String>>> listPkgs() {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("versions_as_list", true);
        return new LocalCall<>("pkg.list_pkgs", Optional.empty(), Optional.of(args),
                new TypeToken<Map<String, List<String>>>(){});
    }

    /**
     * Call 'pkg.info_installed' API.
     *
     * @param attributes list of attributes that should be included in the result
     * @param reportErrors if true will return an error message instead of corrupted text
     * @param packages optional give package names, otherwise return info about all packages
     * @return the call
     */
    public static LocalCall<Map<String, Info>> infoInstalled(List<String> attributes,
            boolean reportErrors, String... packages) {
        LinkedHashMap<String, Object> kwargs = new LinkedHashMap<>();
        kwargs.put("attr", attributes.stream().collect(Collectors.joining(",")));
        if (reportErrors) {
            kwargs.put("errors", "report");
        }
        return new LocalCall<>("pkg.info_installed", Optional.of(Arrays.asList(packages)),
                Optional.of(kwargs), new TypeToken<Map<String, Info>>(){});
    }

    public static LocalCall<Map<String, Info>> infoAvailable(String... packages) {
        return new LocalCall<>("pkg.info_available", Optional.of(Arrays.asList(packages)),
                Optional.empty(), new TypeToken<Map<String, Info>>(){});
    }

    /**
     * Call 'pkg.install' API.
     *
     * @param refresh refresh repos before installation
     * @param pkgs list of packages
     * @return the call
     */
    public static LocalCall<Map<String, Object>> install(boolean refresh,
            List<String> pkgs) {
        LinkedHashMap<String, Object> kwargs = new LinkedHashMap<>();
        kwargs.put("refresh", refresh);
        kwargs.put("pkgs", pkgs);
        return new LocalCall<>("pkg.install", Optional.empty(), Optional.of(kwargs),
                new TypeToken<Map<String, Object>>(){});
    }

    /**
     * @param refresh set true to perform a refresh before the installation
     * @param pkgs map of packages (name to version) to be installed
     * @return the LocalCall object
     */
    public static LocalCall<Map<String, Object>> install(boolean refresh,
            Map<String, String> pkgs) {
        LinkedHashMap<String, Object> kwargs = new LinkedHashMap<>();
        kwargs.put("refresh", refresh);
        kwargs.put("pkgs", preparePkgs(pkgs));
        return new LocalCall<>("pkg.install", Optional.empty(), Optional.of(kwargs),
                new TypeToken<Map<String, Object>>(){});
    }

    /**
     * @param pkgs map of packages (name to version) to be removed
     * @return the LocalCall object
     */
    public static LocalCall<Map<String, Object>> remove(Map<String, String> pkgs) {
        LinkedHashMap<String, Object> kwargs = new LinkedHashMap<>();
        kwargs.put("pkgs", preparePkgs(pkgs));
        return new LocalCall<>("pkg.remove", Optional.empty(), Optional.of(kwargs),
                new TypeToken<Map<String, Object>>(){});
    }

    public static LocalCall<Boolean> upgradeAvailable(String packageName) {
        return new LocalCall<>("pkg.upgrade_available",
                Optional.of(Arrays.asList(packageName)), Optional.empty(),
                new TypeToken<Boolean>(){});
    }

    public static LocalCall<String> latestVersion(String packageName) {
        return new LocalCall<>("pkg.latest_version",
                Optional.of(Arrays.asList(packageName)), Optional.empty(),
                new TypeToken<String>(){});
    }

    public static LocalCall<Map<String, String>> latestVersion(String firstPackageName,
            String secondPackageName, String... packages) {
        return new LocalCall<>("pkg.latest_version",
                Optional.of(Arrays.asList(firstPackageName, secondPackageName, packages)),
                Optional.empty(), new TypeToken<Map<String, String>>(){});
    }

    /**
     * From a given map (package name -> version), create a list of maps with just one
     * element each. This is how Salt requires us to send the 'pkgs' argument when multiple
     * packages should be installed or removed.
     *
     * @param pkgs map with packages (name -> version)
     * @return list of maps with one element each
     */
    private static List<Map<String, String>> preparePkgs(Map<String, String> pkgs) {
        return pkgs.entrySet().stream()
                .map(e -> Collections.unmodifiableMap(Stream.of(e)
                        .collect(Collectors.<Map.Entry<String, String>, String, String>
                                toMap(Map.Entry::getKey, Map.Entry::getValue))))
                .collect(Collectors.toList());
    }
}
