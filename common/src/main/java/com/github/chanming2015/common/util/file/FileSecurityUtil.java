package com.github.chanming2015.common.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryFlag;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 
 * Create Date:2016年6月29日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class FileSecurityUtil
{

    private static final Logger logger = LoggerFactory.getLogger(FileSecurityUtil.class);
    private static final boolean isPosix = FileSystems.getDefault().supportedFileAttributeViews()
            .contains("posix");

    private static final Set<AclEntryPermission> perms = EnumSet.noneOf(AclEntryPermission.class);

    static
    {
        perms.add(AclEntryPermission.READ_DATA);
        perms.add(AclEntryPermission.READ_ATTRIBUTES);
        perms.add(AclEntryPermission.READ_NAMED_ATTRS);
        perms.add(AclEntryPermission.READ_ACL);
        perms.add(AclEntryPermission.WRITE_DATA);
        perms.add(AclEntryPermission.WRITE_ATTRIBUTES);
        perms.add(AclEntryPermission.WRITE_NAMED_ATTRS);
        perms.add(AclEntryPermission.WRITE_ACL);
        perms.add(AclEntryPermission.APPEND_DATA);
        perms.add(AclEntryPermission.SYNCHRONIZE);
    }

    public static SeekableByteChannel getSeekableByteChannel(String filePath)
    {
        Path path = new File(filePath).toPath();
        Set<OpenOption> options = new HashSet<OpenOption>();
        options.add(StandardOpenOption.CREATE);
        options.add(StandardOpenOption.WRITE);
        FileAttribute<?> fa = null;
        if (isPosix)
        {
            Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
            fa = PosixFilePermissions.asFileAttribute(perms);
        }
        else
        {
            String username = System.getProperty("user.name");
            UserPrincipal user = null;
            try
            {
                user = path.getFileSystem().getUserPrincipalLookupService()
                        .lookupPrincipalByName(username);
            }
            catch (IOException e)
            {
                logger.error("unknow user:%s", username);
            }

            final AclEntry entry = AclEntry.newBuilder().setType(AclEntryType.ALLOW)
                    .setPrincipal(user).setPermissions(perms)
                    .setFlags(AclEntryFlag.FILE_INHERIT, AclEntryFlag.DIRECTORY_INHERIT).build();

            FileAttribute<List<AclEntry>> aclattrs = new FileAttribute<List<AclEntry>>()
            {

                @Override
                public String name()
                {
                    return "acl:acl";
                }

                @Override
                public List<AclEntry> value()
                {
                    ArrayList<AclEntry> list = new ArrayList<AclEntry>(1);
                    list.add(entry);
                    return list;
                }
            };

            fa = aclattrs;
        }

        SeekableByteChannel sbc = null;
        try
        {
            sbc = Files.newByteChannel(path, options, fa);
        }
        catch (IOException e)
        {
            logger.error("newByteChannel error");
        }

        return sbc;
    }

}
