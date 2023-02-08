//
//  RequestBridge.m
//  UnityPluginBridge
//
//  Created by  on 2020/01/05.
//  Copyright © 2020 Company. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UnityPluginProtocol.h"
#import <UnityPluginCore/UnityPluginCore-swift.h>

void RequestBridge(
    int id,
    const char* packageName,
    const char* className,
    const char* parameter)
{
    UnityPluginProtocol* pluginProtocol = [[UnityPluginProtocol alloc] init];
    
    [[Plugin GetInstance] requestWithPluginProtocol:pluginProtocol
        id:id
        packageName:[NSString stringWithUTF8String:packageName]
        className:[NSString stringWithUTF8String:className]
        parameter:parameter != nil ?  [NSString stringWithUTF8String:parameter] : nil];
}
